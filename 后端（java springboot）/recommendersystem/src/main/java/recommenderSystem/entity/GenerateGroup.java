package recommenderSystem.entity;

import recommenderSystem.domain.User;

import java.util.ArrayList;
import java.util.List;

public class GenerateGroup {
    private List<KUser> initPlayers;
    private List<KUser> players;
    public static List<KUser> clusterHeart;
    private int K;

    public GenerateGroup(List<KUser>list,int K){
        players = list;
        initPlayers = new ArrayList<>();
        clusterHeart = new ArrayList<>();
        this.K = K;
        for(int i=0;i<K;i++) {
            initPlayers.add(players.get(i));
        }
    }

    public List<KUser>[] cluster(){
        List<KUser>[] res = new ArrayList[K];
        boolean centerchange = true;

        while(centerchange){
            centerchange = false;
            for(int i=0;i<K;i++){
                res[i] = new ArrayList<>();
            }
            //循环每个用户，找出每个用户与聚类中心的距离
            for(int i=0;i<players.size();i++){
                KUser p = players.get(i);
                double[] dists = new double[K];
                for(int j=0;j<initPlayers.size();j++){
                    KUser initP = initPlayers.get(j);
                    double dist = distance(initP,p);
                    dists[j] = dist;
                }
                //找出距离最小的用户的下标
                int dist_index = computOrder(dists);
                res[dist_index].add(p);
            }
            //判断新旧聚类中心是否一样，不一样则创建新的聚类中心
            for(int i=0;i<K;i++){
                KUser player_new = findNewCenter(res[i]);
                KUser player_old = initPlayers.get(i);
                if(!isPlayerEqual(player_new,player_old)){
                    centerchange = true;
                    initPlayers.set(i,player_new);
                    clusterHeart.clear();
                }else{
                    clusterHeart.add(player_new);
                }
            }
        }
        return res;
    }
    public boolean isPlayerEqual(KUser p1,KUser p2){
        if(p1==p2) return true;
        if(p1==null||p2==null) return false;
//        boolean flag = true;
//        double[]s1 = p1.getWeights();
//        double[]s2 = p2.getWeights();
//        for(int i=0;i<s2.length;i++){
//            if(s1[i]!=s2[i]){
//                flag = false;
//                break;
//            }
//        }
//        return flag;
        return false;
    }
    //找出新的聚类中心
    public KUser findNewCenter(List<KUser> list){
        KUser u = new KUser();
        if(list==null||list.size()==0) return u;
        int len = list.get(0).getWeights().length;
        double []ds = new double[len];
        for(int i=0;i<len;i++){
            for(int j=0;j<list.size();j++){
//                ds[i] += list.get(j).getWeights()[i];
            }
        }
        for(int i=0;i<len;i++){
            ds[i] = ds[i]/list.size();
        }
        u.setWeights(ds);
        return u;
    }
    //比较距离，找出最小距离下标
    public int computOrder(double[] dists){
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

    public double distance(KUser p1,KUser p2){
        double dis = 0;
        try{
            double[] s1 = p1.getWeights();
            double[] s2 = p2.getWeights();
            for (int i = 0; i < s2.length; i++) {
                dis+=Math.pow(s1[i]-s2[i],2);
            }
        }
        catch(Exception exception){}

        return Math.sqrt(dis);
    }
}
