package com.nectar.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.BatchGetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.Select;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.nectar.repository.YearInReviewViewsRecord.YEAR_INDEX_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class YearInReviewViewsRecordRepository {

    private static final Class<YearInReviewViewsRecord> clazz = YearInReviewViewsRecord.class;
    private final DynamoDBMapper dynamoDBMapper;
    private final AmazonDynamoDB amazonDynamoDB;
    private static final String YEAR_FIELD_NAME = "year";

    public void save(YearInReviewViewsRecord record) {
        dynamoDBMapper.save(record);
    }

    public Optional<YearInReviewViewsRecord> findById(String id) {
        return Optional.ofNullable(dynamoDBMapper.load(clazz, id));
    }

    public long getViewsCount() {
        return amazonDynamoDB.describeTable(getCurrentTableName()).getTable().getItemCount();
    }

    public String getCurrentTableName() {
        return YearInReviewViewsRecord.TABLE_NAME;
    }

    public Set<Integer> getYears() {
        Index index = new DynamoDB(amazonDynamoDB).getTable(getCurrentTableName()).getIndex(YEAR_INDEX_NAME);
        ScanSpec scanSpec = new ScanSpec().withSelect(Select.SPECIFIC_ATTRIBUTES).withAttributesToGet(YEAR_FIELD_NAME);
        ItemCollection<ScanOutcome> items = index.scan(scanSpec);
        Iterator<Item> pageIterator = items.iterator();
        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(pageIterator, Spliterator.ORDERED), false)
            .map(x -> x.getInt(YEAR_FIELD_NAME))
            .collect(Collectors.toSet());
    }

    public Integer getViewsCountForYear(Integer year) {
        Index index = new DynamoDB(amazonDynamoDB).getTable(getCurrentTableName()).getIndex(YEAR_INDEX_NAME);
        QuerySpec querySpec = new QuerySpec().withHashKey(YEAR_FIELD_NAME, year).withSelect(Select.COUNT);
        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(index.query(querySpec).pages().iterator(), Spliterator.ORDERED), false)
            .map(x -> x.getLowLevelResult().getQueryResult().getCount())
            .reduce(Integer::sum).orElse(0);
    }

    public List<QueryResult> getViewsForYearPrevious(Integer year) {
        Index index = new DynamoDB(amazonDynamoDB).getTable(getCurrentTableName()).getIndex(YEAR_INDEX_NAME);
        QuerySpec querySpec = new QuerySpec().withHashKey(YEAR_FIELD_NAME, year).withSelect(Select.ALL_ATTRIBUTES);
        List<QueryResult> queryResults = StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(index.query(querySpec).pages().iterator(), Spliterator.ORDERED), false)
                .map(x -> x.getLowLevelResult().getQueryResult())
                .collect(Collectors.toList());

        System.out.println("******* query result size = "+ queryResults.size());
        return queryResults;
    }

    public void getViewsForYear(Integer year) throws IOException {

        //create a file

        File csvFile= new File("year-review.csv");
        PrintWriter out = new PrintWriter(new FileOutputStream(csvFile,true));


        Index index = new DynamoDB(amazonDynamoDB).getTable(getCurrentTableName()).getIndex(YEAR_INDEX_NAME);
        QuerySpec querySpec = new QuerySpec().withHashKey(YEAR_FIELD_NAME, year).withSelect(Select.SPECIFIC_ATTRIBUTES)
                .withMaxPageSize(Integer.parseInt("2"))
               .withAttributesToGet("firstViewDate","cardNumber");
        ItemCollection<QueryOutcome> items = index.query(querySpec);
        int pageNum = 0;
        for (Page<Item, QueryOutcome> page : items.pages()) {

            // Process each item on the current page
            Iterator<Item> item = page.iterator();

            //write using spring batch

            while (item.hasNext()) {
                //System.out.println(item.next().toJSONPretty());
                out.println(item.next().toJSON());
                //need to write in
            }

            pageNum = pageNum+1;


        }

        out.flush();
        out.close();

    }

    public void retrieveMultipleItemsBatchGet() {

        try {

            TableKeysAndAttributes tableKeysAndAttributes = new TableKeysAndAttributes(getCurrentTableName());
            // Add a partition key
            tableKeysAndAttributes.addHashOnlyPrimaryKeys("yirId",YEAR_INDEX_NAME);

            tableKeysAndAttributes.withAttributeNames(List.of("firstViewDate","cardNumber"));

            tableKeysAndAttributes.withProjectionExpression(tableKeysAndAttributes.getProjectionExpression());

            System.out.println("Making the request.");

            System.out.println(tableKeysAndAttributes.getPrimaryKeys());

            System.out.println(tableKeysAndAttributes.getAttributeNames());




            BatchGetItemSpec batchGetItemSpec = new BatchGetItemSpec().withTableKeyAndAttributes(tableKeysAndAttributes);

            BatchGetItemOutcome outcome = new DynamoDB(amazonDynamoDB).batchGetItem(batchGetItemSpec);

            Map<String, KeysAndAttributes> unprocessed = null;

            do {
                for (String tableName : outcome.getTableItems().keySet()) {
                    System.out.println("Items in table " + tableName);
                    List<Item> items = outcome.getTableItems().get(tableName);
                    for (Item item : items) {
                        System.out.println(item.toJSONPretty());
                    }
                }

                // Check for unprocessed keys which could happen if you exceed
                // provisioned
                // throughput or reach the limit on response size.
                unprocessed = outcome.getUnprocessedKeys();

                if (unprocessed.isEmpty()) {
                    System.out.println("No unprocessed keys found");
                }
                else {
                    System.out.println("Retrieving the unprocessed keys");
                    outcome = new DynamoDB(amazonDynamoDB).batchGetItemUnprocessed(unprocessed);
                }

            } while (!unprocessed.isEmpty());

        }
        catch (Exception e) {
            System.err.println("Failed to retrieve items.");
            System.err.println(e.getMessage());
        }
    }

}