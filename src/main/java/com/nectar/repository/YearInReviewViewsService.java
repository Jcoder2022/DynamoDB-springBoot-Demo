package com.nectar.repository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.function.Supplier;

@Slf4j
@Service
@AllArgsConstructor
public class YearInReviewViewsService {

    private final YearInReviewViewsRecordRepository yearInReviewViewsRecordRepository;

    private final Supplier<ZonedDateTime> zonedDateTimeSupplier;

    @Async
    public void saveViewMetrics(String cardNumber, String yirId, Integer year) {
        if (!hasReportBeenSeen(yirId)) {
            saveFirstViewDateTime(cardNumber, yirId, year);
        }
    }


    public void saveFirstViewDateTime(String cardNumber, String yirId, int year) {
        log.info("Saving first view date time {} {} {}", cardNumber, yirId, year);
        ZonedDateTime firstViewDateTime = zonedDateTimeSupplier.get();
        yearInReviewViewsRecordRepository.save(YearInReviewViewsRecord.builder()
                .cardNumber(cardNumber)
                .yirId(yirId)
                .year(year)
                .firstViewDate(firstViewDateTime).build());
    }

    public boolean hasReportBeenSeen(String yirId) {
        return yearInReviewViewsRecordRepository.findById(yirId).isPresent();
    }

    public long getViewsCountForYear(int year) {
        return yearInReviewViewsRecordRepository.getViewsCountForYear(year);
    }

    public Set<Integer> getYears() {
        return yearInReviewViewsRecordRepository.getYears();
    }

}
