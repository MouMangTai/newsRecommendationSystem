package recommenderSystem.controller;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.apache.mahout.cf.taste.common.TasteException;

import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.*;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recommenderSystem.domain.New;
import recommenderSystem.domain.Rate;
import recommenderSystem.domain.User;
import recommenderSystem.entity.KUser;
import recommenderSystem.entity.ResponseResult;
import recommenderSystem.service.NewsServices;
import recommenderSystem.service.RateServices;
import recommenderSystem.service.TypeServices;
import recommenderSystem.service.UsersServices;

import java.lang.reflect.Array;
import java.util.*;

@RestController
@RequestMapping("Rec")
public class RecController {
    @Autowired
    private NewsServices newsServices;
    @Autowired
    private UsersServices usersServices;
    @Autowired
    private TypeServices typeServices;
    @Autowired
    private RateServices rateServices;

    @GetMapping("recByContent/{nid}")
    public ResponseResult<List<New>> recByContent(@PathVariable int nid) {
        New curNew = newsServices.findById(nid);
        List<New> news = newsServices.all();


        List<New> recNews = new ArrayList<>();
        List<String> totalKeyWord = new ArrayList<>();
        //计算语料库
        for (New n : news) {
            String content = n.getNewContent();
            KeyWordComputer key = new KeyWordComputer(3);
            Iterator<Keyword> it = key.computeArticleTfidf(content).iterator();
            while (it.hasNext()) {
                Keyword key2 = (Keyword) it.next();
                System.out.println(key2.toString() + "---" + key2.getScore() + "---" + key2.getName() + "---" + key2.getFreq());

            }
        }

        //计算向量
//        for (New n:news){
//            List<Integer> vector = new ArrayList<>();
//            String content = n.getNewContent();
//            KeyWordComputer key = new KeyWordComputer(5);
//            Iterator<Keyword> it = key.computeArticleTfidf(content).iterator();
//            while(it.hasNext()){
//                Keyword key2 = (Keyword)it.next();
//                System.out.println(key2.toString()+"---"+key2.getScore()+"---"+key2.getName()+"---"+key2.getFreq());
//                int i = totalKeyWord.indexOf(key2.getName());
//                vector.add(i, (int) key2.getScore());
//            }
//        }

        return new ResponseResult<>(ResponseResult.CodeStatus.OK, "获取成功", news);
    }
    @GetMapping("recByUser/{id}")
    public ResponseResult<List<New>> recByUser(@PathVariable int id) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("root");
        dataSource.setPassword("admin");
        dataSource.setDatabaseName("news_recommendation_system");
        JDBCDataModel dataModel = new MySQLJDBCDataModel(dataSource, "score", "uid", "nid", "score", "time");
        DataModel model = dataModel;
        List<New> news = new ArrayList<>();
        try {
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(5, similarity, model);
            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            List<RecommendedItem> recNews = recommender.recommend(id, 10);
            if (recNews.size() > 0) {
                for (RecommendedItem r : recNews) {
                    news.add(newsServices.findById((int) r.getItemID()));
                }

            } else {
                System.out.println("无任何物品推荐");
            }
            if (news.size() < 10) {
                User user = usersServices.findById(id);

                int left = 10 - news.size();
                List<New> total = new ArrayList<>();
                String[] types = user.getPrefer().split(",");
                for (String type : types) {
                    int index = typeServices.findIdByType(type);
                    List<New> curnew = newsServices.findByTypeAndNum(index, 10);
                    for (New n : curnew) {
                        total.add(n);
                    }
                }
                Set<Integer> S = new HashSet<>();
                Random r = new Random();
                for (int i = 0; i < left; i++) {
                    int random = r.nextInt(10 * types.length);
                    while (S.contains(random)) {
                        random = r.nextInt(10 * types.length);
                    }
                    S.add(random);
                    news.add(total.get(random));
                }
//                news.add(newsServices.findById(news.size()*2+1));
            }

        } catch (TasteException e) {
            e.printStackTrace();
        }
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, "获取成功", news);
    }
    @GetMapping("recByItem/{id}")
    public ResponseResult<List<New>> recByItem(@PathVariable int id) {
        List<New> news = new ArrayList<>();
        List<Rate> rs = rateServices.findRateByUid(id);
        if (rs.size() == 0) {
            User user = usersServices.findById(id);

            int left = 10 - news.size();
            List<New> total = new ArrayList<>();
            String[] types = user.getPrefer().split(",");
            for (String type : types) {
                int index = typeServices.findIdByType(type);
                List<New> curnew = newsServices.findByTypeAndNum(index, 10);
                for (New n : curnew) {
                    total.add(n);
                }
            }
            Set<Integer> S = new HashSet<>();
            Random r = new Random();
            for (int i = 0; i < left; i++) {
                int random = r.nextInt(10 * types.length);
                while (S.contains(random)) {
                    random = r.nextInt(10 * types.length);
                }
                S.add(random);
                news.add(total.get(random));
            }
            return new ResponseResult<>(ResponseResult.CodeStatus.OK, "获取成功", news);
        }
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("root");
        dataSource.setPassword("admin");
        dataSource.setDatabaseName("news_recommendation_system");
        JDBCDataModel dataModel = new MySQLJDBCDataModel(dataSource, "score", "uid", "nid", "score", "time");
        DataModel model = dataModel;

        try {
            ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
            ItemBasedRecommender recommender = new GenericItemBasedRecommender(model, similarity);

            List<RecommendedItem> recNews = recommender.recommend(id, 10);
            if (recNews.size() > 0) {
                for (RecommendedItem r : recNews) {
                    news.add(newsServices.findById((int) r.getItemID()));
                }
            } else {
                System.out.println("无任何物品推荐");
            }
            if (news.size() < 10) {
                User user = usersServices.findById(id);

                int left = 10 - news.size();
                List<New> total = new ArrayList<>();
                String[] types = user.getPrefer().split(",");
                for (String type : types) {
                    int index = typeServices.findIdByType(type);
                    List<New> curnew = newsServices.findByTypeAndNum(index, 10);
                    for (New n : curnew) {
                        total.add(n);
                    }
                }
                Set<Integer> S = new HashSet<>();
                Random r = new Random();
                for (int i = 0; i < left; i++) {
                    int random = r.nextInt(10 * types.length);
                    while (S.contains(random)) {
                        random = r.nextInt(10 * types.length);
                    }
                    S.add(random);
                    news.add(total.get(random));
                }
            }

        } catch (TasteException e) {
            e.printStackTrace();
        }
        return new ResponseResult<>(ResponseResult.CodeStatus.OK, "获取成功", news);
    }
    @GetMapping("recByKmeans/{id}")
    public ResponseResult<List<New>> recByKmeans(@PathVariable int id) {
        List<New> news = new ArrayList<>();
        List<Object> objs = read();
        List<KUser> players = (List<KUser>) objs.get(0);
        List<KUser> initPlayers = new ArrayList<>();
        List<KUser> clusterHeart = new ArrayList<>();
        int K = 3;
        for (int i = 0; i < K; i++) {
            initPlayers.add(players.get(i));
        }
        List<KUser>[] res = null;
        List<KUser> recUser = new ArrayList<>();
//        int t = K;
//        while(recUser.size()<=0){
            res = cluster(initPlayers, players, clusterHeart, K);
            for(int i=0;i<K;i++){
                boolean flag = true;
                for(KUser user:res[i]){
                    if(user.getUid()==id){
                        recUser = res[i];
                        flag = false;
                        recUser.remove(user);
                        break;
                    }
                }
                if(flag==false) break;
            }
//            t--;
//        }
        int p = 5;
        while(news.size()<10&&p>=3){
            for(KUser user:recUser){
                System.out.println(user.getUid());
                List<Rate> rates = rateServices.findRateByUid(user.getUid());
                for(Rate rate:rates){
                    if(rate.getUid()==id){
                        continue;
                    }
                    if(rate.getScore()==p){
                        New neww = newsServices.findById(rate.getNid());
                        if(!news.contains(neww))
                            news.add(neww);

                    }
                    if(news.size()>=10) break;
                }
                if(news.size()>=10) break;
            }
            p--;
        }
        Collections.shuffle(news);
        if (news.size() < 10) {
            User user = usersServices.findById(id);

            int left = 10 - news.size();
            List<New> total = new ArrayList<>();
            String[] types = user.getPrefer().split(",");
            for (String type : types) {
                int index = typeServices.findIdByType(type);
                List<New> curnew = newsServices.findByTypeAndNum(index, 10);
                for (New n : curnew) {
                    total.add(n);
                }
            }
            Set<Integer> S = new HashSet<>();
            Random r = new Random();
            for (int i = 0; i < left; i++) {
                int random = r.nextInt(10 * types.length);
                while (S.contains(random)) {
                    random = r.nextInt(10 * types.length);
                }
                S.add(random);
                New neww = total.get(random);
                if(!news.contains(neww))
                    news.add(neww);
            }
        }

        return new ResponseResult<>(ResponseResult.CodeStatus.OK, "获取成功", news);
    }
    //读取数据库中的数据
    public List<Object> read() {
        double[][] weight;
        List<Integer> uids = usersServices.findALLUid();
        List<Integer> nids = rateServices.findALLNid();
        weight = new double[uids.size()][nids.size()];
        List<Rate> rates = rateServices.all();
        List<KUser> kUsers = new ArrayList<>();
        for (Rate rate : rates) {
            weight[uids.indexOf(rate.getUid())][nids.indexOf(rate.getNid())] = rate.getScore();
        }

        for (int i = 0; i < weight.length; i++) {
            KUser kUser = new KUser();
            kUser.setUid(uids.get(i));
            double[] w = new double[weight[0].length];
            for (int j = 0; j < weight[0].length; j++) {
                w[j] = weight[i][j];
                kUser.setWeights(w);
            }
            kUsers.add(kUser);
        }
        List<Object> objs = new ArrayList<>();
        objs.add(kUsers);
        objs.add(weight);
        objs.add(uids);
        objs.add(nids);
        objs.add(rates);


        return objs;
    }
    //根据用户评分聚类
    public List<KUser>[] cluster(List<KUser> initPlayers, List<KUser> players, List<KUser> clusterHeart, int K) {
        //聚类结果
        List<KUser>[] res = new ArrayList[K];
        //循环继续判断:当簇心不变时循环结束
        boolean centerchange = true;

        while (centerchange) {
            centerchange = false;
            for (int i = 0; i < K; i++) {
                res[i] = new ArrayList<>();
            }
            //循环每个用户，找出每个用户与聚类中心的距离，并归入到各个聚类中
            for (int i = 0; i < players.size(); i++) {
                KUser p = players.get(i);
                double[] dists = new double[K];
                for (int j = 0; j < initPlayers.size(); j++) {
                    KUser initP = initPlayers.get(j);
                    double dist = distance(initP, p);
                    dists[j] = dist;
                }
                //找出距离最小的用户的下标
                int dist_index = computOrder(dists);
                res[dist_index].add(p);
            }
            //判断新旧聚类中心是否一样，不一样则创建新的聚类中心
            for (int i = 0; i < K; i++) {
                KUser player_new = findNewCenter(res[i]);
                KUser player_old = initPlayers.get(i);
                if (!isPlayerEqual(player_new, player_old)) {
                    centerchange = true;
                    initPlayers.set(i, player_new);
                    clusterHeart.clear();
                } else {
                    clusterHeart.add(player_new);
                }
            }
        }
        System.out.println("聚类结果：共分为" + K + "个组");
        for (int i = 0; i < res.length; i++) {
            System.out.println("-----第" + i + "组-----");
            for (KUser user : res[i]) {
                System.out.print("用户:" + user.getUid() + " 评分");
                double[] weights = user.getWeights();
                for (double w : weights) {
                    System.out.print(w + ",");
                }
                System.out.println();
            }
            System.out.println();
        }
        return res;
    }
    //判断两个簇心是否相同
    public boolean isPlayerEqual(KUser p1, KUser p2) {
        if (p1 == p2) return true;
        if (p1 == null || p2 == null) return false;
        boolean flag = true;
        double[] s1 = p1.getWeights();
        double[] s2 = p2.getWeights();
        for (int i = 0; i < s2.length; i++) {
            if (s1[i] != s2[i]) {
                flag = false;
                break;
            }
        }
        return flag;
    }
    //找出新的聚类中心
    public KUser findNewCenter(List<KUser> list) {
        KUser u = new KUser();
        if (list == null || list.size() == 0) return u;
        int len = list.get(0).getWeights().length;
        double[] ds = new double[len];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < list.size(); j++) {
                ds[i] += list.get(j).getWeights()[i];
            }
        }
        for (int i = 0; i < len; i++) {
            ds[i] = ds[i] / list.size();
        }

        u.setWeights(ds);
        return u;
    }
    //比较距离，找出最小距离下标
    public int computOrder(double[] dists) {
        double min = dists[0];
        int index = 0;
        for(int i=1;i<dists.length;i++){
            if(dists[i]<min){
                min = dists[i];
                index = i;
            }
        }
        return index;
    }
    //判断距离，欧几里得算法,最快
    public double distance(KUser p1, KUser p2) {
        double dis = 0;
        try {
            double[] s1 = p1.getWeights();
            double[] s2 = p2.getWeights();
            for (int i = 0; i < s2.length; i++) {
                dis += Math.pow(s1[i] - s2[i], 2);
            }
        } catch (Exception exception) {
        }

        return Math.sqrt(dis);
    }
    //计算用户之间的相似度
//    public double[] generateSimilarity(KUser u1,List<KUser> users){
//        double []res = new double[users.size()];
//        for(int i=0;i<users.size();i++){
//            if(isPlayerEqual(u1,users.get(i))){
//                res[i] = 1;
//                continue;
//            }
//            res[i] = distance(u1,users.get(i))/users.size();
//        }
//        System.out.println("-----计算用户相似度-----");
//
//        for(int i=0;i<users.size();i++){
//            System.out.println(users.get(i).getUid()+":"+res[i]);
//        }
//        return res;
//    }
}
