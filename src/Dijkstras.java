import java.util.*;

/**
 * 狄克斯特拉算法实现
 */
public class Dijkstras {
    public static void main(String[] args) {
        //1.数据结构的构造
        //1.1.创建各节点的邻居节点开销散列表的散列表
        Map<String, Map<String, Integer>> nodesNeighborsCostMap = new HashMap<>();

        /*-----------------------------put数据-----------------------------*/
        Map<String, Integer> aNeighborsCostMap = new HashMap<>();
        aNeighborsCostMap.put("B", 10);
        //aNeighborCost.put("C", 2);
        nodesNeighborsCostMap.put("A", aNeighborsCostMap);

        Map<String, Integer> bNeighborCost = new HashMap<>();
        bNeighborCost.put("D", 20);
        //bNeighborCost.put("E", 2);
        nodesNeighborsCostMap.put("B", bNeighborCost);

        Map<String, Integer> cNeighborCost = new HashMap<>();
        cNeighborCost.put("B", 1);
        //cNeighborCost.put("E", 7);
        nodesNeighborsCostMap.put("C", cNeighborCost);

        Map<String, Integer> dNeighborCost = new HashMap<>();
        dNeighborCost.put("C", 1);
        dNeighborCost.put("E", 30);
        nodesNeighborsCostMap.put("D", dNeighborCost);

        //Map<String, Integer> eNeighborCost = new HashMap<>();
        //eNeighborCost.put("F", 1);
        //neighborCostMap.put("E", eNeighborCost);

        nodesNeighborsCostMap.put("E", null);//终点无邻居


        //1.2.创建记录起点到各节点最少花销的散列表
        Map<String, Integer> nodesLowestFullCostMap = new HashMap<>();
        //赋初值（随着算法运行，动态更新）
        nodesLowestFullCostMap.put("A", 0);//起点到起点的花销为0
        nodesLowestFullCostMap.put("B", Integer.MAX_VALUE);//表示值为最大精度，无穷大的意思
        nodesLowestFullCostMap.put("C", Integer.MAX_VALUE);
        nodesLowestFullCostMap.put("D", Integer.MAX_VALUE);
        nodesLowestFullCostMap.put("E", Integer.MAX_VALUE);
        //nodeLowestCostMap.put("F", Integer.MAX_VALUE);


        //1.3.创建最佳父节点散列表（记录各节点的最佳父节点，随着算法运行而动态更新）
        Map<String, String> nodesBestParentMap = new HashMap<>();


        //1.4.创建已处理的节点的数组（当节点的所有邻居节点处理完，便将该节点放入此数组，避免重复处理）
        List<String> processedNodeList = new ArrayList<>();



        //2.执行算法
        int nodeCount = nodesNeighborsCostMap.size();//执行次数依节点数量而定
        for (int i = 0; i < nodeCount; i++) {
            processor(nodesNeighborsCostMap, nodesLowestFullCostMap, nodesBestParentMap, processedNodeList);
        }


        //3.打印结果（懒，未细致打印）
        System.out.println("各节点最佳父节点：" + nodesBestParentMap);
        System.out.println("各节点最低全开销：" + nodesLowestFullCostMap);

    }

    /**
     * 算法主体实现
     * @param nodesNeighborsCostMap 节点与（其邻居节点与开销的映射）的映射
     * @param nodesLowestFullCostMap 节点与其最低全开销的映射
     * @param nodesBestParentMap 节点与其最佳父节点的映射
     * @param processedNodeList 已更新完邻居节点最低全开销的节点的列表，即已被算法处理完的节点的列表
     */
    private static void processor(Map<String, Map<String, Integer>> nodesNeighborsCostMap, Map<String, Integer> nodesLowestFullCostMap, Map<String, String> nodesBestParentMap, List<String> processedNodeList) {
        //开始处理（单个节点的处理逻辑）
        //获得待处理的当前节点
        String node = getSmallestLowestFullCostNode(nodesLowestFullCostMap, processedNodeList);
        //邻居节点们及其开销
        Map<String, Integer> neighborsCostMap = nodesNeighborsCostMap.get(node);
        if (neighborsCostMap == null) {//为空表示当前节点为终点，不处理
            return;
        }
        //当前节点的最低全开销
        Integer nodeLowestFullCost = nodesLowestFullCostMap.get(node);
        //邻居们
        Set<String> neighbors = neighborsCostMap.keySet();
        //开始遍历处理
        for (String neighbor : neighbors) {
            //邻居节点的开销
            Integer neighborCost = neighborsCostMap.get(neighbor);
            //邻居节点经由当前节点的全开销
            Integer neighborNewFullCost = nodeLowestFullCost + neighborCost;
            //已记录的邻居节点的最低全开销
            Integer neighborLowestFullCost = nodesLowestFullCostMap.get(neighbor);
            //判断邻居节点经由当前节点的全开销是否小于已记录的最低全开销，是则更新最低全开销及父节点
            if (neighborNewFullCost < neighborLowestFullCost) {
                //将邻居节点的最低全开销改为经由当前节点的全开销
                nodesLowestFullCostMap.put(neighbor, neighborNewFullCost);
                //将邻居节点的父节点改为当前节点
                nodesBestParentMap.put(neighbor, node);
            }
        }
        //放进已处理数组
        processedNodeList.add(node);
    }

    /**
     * 算法部分结构实现-查找最小的最低全开销的节点
     * @param nodesLowestFullCostMap 节点与其最低全开销的映射
     * @param processedNodeList 已更新完邻居节点最低全开销的节点的列表，即已被算法处理完的节点的列表
     * @return 最小的最低全开销的节点
     */
    public static String getSmallestLowestFullCostNode(Map<String, Integer> nodesLowestFullCostMap, List<String> processedNodeList) {
        Set<String> nodes = nodesLowestFullCostMap.keySet();
        Integer smallestLowestFullCost = Integer.MAX_VALUE;//定义最小的最低全开销，初值无穷大
        String smallestLowestFullCostNode = null;//定义最小的最低全开销的节点
        for (String node : nodes) {
            //判断是否处理过，从未处理的最低全开销节点中取得最低的最低全开销节点
            if (!processedNodeList.contains(node)) {
                //获取节点的最低全开销
                Integer nodeLowestFullCost = nodesLowestFullCostMap.get(node);
                //判断是否是最低的最低全开销
                if (nodeLowestFullCost <= smallestLowestFullCost) {
                    smallestLowestFullCost = nodeLowestFullCost;
                    smallestLowestFullCostNode = node;
                }
            }
        }
        return smallestLowestFullCostNode;
    }

}