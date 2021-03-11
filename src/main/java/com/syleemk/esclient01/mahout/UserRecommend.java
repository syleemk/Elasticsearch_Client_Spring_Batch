package com.syleemk.esclient01.mahout;

import com.syleemk.esclient01.Esclient01Application;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.boot.SpringApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UserRecommend {
    public static void main(String[] args) throws IOException, TasteException {
        ClassPathResource classPathResource = new ClassPathResource("data/data.csv");
        File file = classPathResource.getFile();
//        File file1 = new File("C:\\Users\\sylee\\IdeaProjects\\esclient01\\src\\main\\resources\\data\\data.csv");


        DataModel dm = new FileDataModel(file);
        UserSimilarity usim = new PearsonCorrelationSimilarity(dm);

        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, usim, dm);
        UserBasedRecommender recommender = new GenericUserBasedRecommender(dm, neighborhood, usim);

        List<RecommendedItem> recommend = recommender.recommend(196, 10);
        for (RecommendedItem recommendedItem : recommend) {
            System.out.println("recommendedItem = " + recommendedItem.getItemID());
        }
    }
}
