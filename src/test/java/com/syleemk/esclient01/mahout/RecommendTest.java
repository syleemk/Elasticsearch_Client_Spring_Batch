package com.syleemk.esclient01.mahout;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class RecommendTest {

    @Test
    void recommendTest() throws IOException, TasteException {
        DataModel dm = new FileDataModel(new File("data/data.csv"));
        UserSimilarity usim = new PearsonCorrelationSimilarity(dm);

        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, usim, dm);
        UserBasedRecommender recommender = new GenericUserBasedRecommender(dm, neighborhood, usim);

        List<RecommendedItem> recommend = recommender.recommend(1887029, 10);
        for (RecommendedItem recommendedItem : recommend) {
            System.out.println("recommendedItem = " + recommendedItem);
        }
    }
}
