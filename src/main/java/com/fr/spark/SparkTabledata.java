package com.fr.spark;

import com.fr.data.AbstractTableData;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.storage.StorageLevel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SparkTabledata extends AbstractTableData {

    private String[] columnNames;
    private Object[][] rowData;

    public SparkTabledata() {
        // 创建 Spark 连接
        SparkConf conf = new SparkConf().setAppName("spark-report").setMaster("local");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        // 导入数据源数据, 来源数据库、文件等等 eg. 华东,孙林,饮料,苹果汁,140,sunlin
        JavaRDD<String> lineRDD = readDataSource(jsc);
        this.columnNames = readCoulumnNamesFromDs();
        // 数据持久化，支持多种存储方式，全内存、全磁盘、内存 + 磁盘等等
        lineRDD = lineRDD.persist(StorageLevel.DISK_ONLY());

        // 对数据进行过滤筛选，eg. 过滤销量大于 200 的数据
        JavaRDD<String> filterRDD = lineRDD.filter(new Function<String, Boolean>() {
            public Boolean call(String line) throws Exception {
                String[] datas = line.split(",");
                return Integer.parseInt(datas[4]) > 200;
            }
        });
        // 取过滤后的 10 条明细数据过来
        List<String> result = filterRDD.take(10);
        this.rowData = fillData(columnNames, result);

        // 关闭 Spark 连接
        closeConnection(jsc);
    }

    private void closeConnection(JavaSparkContext jsc) {
        try {
            jsc.stop();
        } catch (Exception e) {
            // ignore
        }
    }

    @NotNull
    private String[] readCoulumnNamesFromDs() {
        return new String[]{"城市", "销售员", "种类", "饮料", "销量", "英文名"};
    }

    private JavaRDD<String> readDataSource(JavaSparkContext jsc) {
        return jsc.textFile("/Users/bokai/Downloads/datasource.csv");
    }

    @NotNull
    private String[][] fillData(String[] columnNames, List<String> result) {
        int columnCount = columnNames.length;
        int rowCount = result.size();
        String [][] datas = new String[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            String[] lineResult = result.get(i).split(",");
            for (int j = 0; j < columnCount; j++) {
                datas[i][j] = lineResult[j];
            }
        }
        return datas;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    public int getRowCount() {
        return rowData.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return rowData[rowIndex][columnIndex];
    }

}