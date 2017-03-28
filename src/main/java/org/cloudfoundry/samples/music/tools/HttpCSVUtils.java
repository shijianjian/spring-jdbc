package org.cloudfoundry.samples.music.tools;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
/**
 * Created by shijian on 23/02/2017.
 */
public class HttpCSVUtils {

    private String fileString;
    private List<String> labels;
    private List<String> rows;

    public HttpCSVUtils(MultipartFile multipartcsv) {
        try {
            byte[] bytes = multipartcsv.getBytes();
            fileString = new String(bytes);
            rows = new ArrayList<>(Arrays.asList(fileString.split("\n")));
            labels = new ArrayList<>(Arrays.asList(rows.get(0).trim().split(",")));
            rows.remove(0);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public List<String> getRows() {
        return rows;
    }

    public List<String> getLabels() { return labels; }

    /**
     * return -1 if there is no such label
     * @param label
     * @return
     */
//    public int labelIndex(String label) {
//        for(int i=0; i<labels.length; i++) {
//            if(label.trim().toLowerCase().equals(labels[i].trim().toLowerCase()))
//                return i;
//        }
//        return -1;
//    }

    public List<String> getSingleRowData(Integer index) {
        String[] data = rows.get(index).trim().split(",");
        List<String> res = new ArrayList<>(Arrays.asList(data));
        return res;
    }

    public HashMap<String, Object> getRowDataMap(Integer index) {
        List<String> currentData = getSingleRowData(index);
        List<String> labels = getLabels();
        HashMap<String, Object> dataMap = new HashMap<>();
        for(int i=0; i<labels.size(); i++) {
            try {
                dataMap.put(labels.get(i), currentData.get(i));
            } catch (IndexOutOfBoundsException e) {
                dataMap.put(labels.get(i), " ");
            }
        }
        return dataMap;
    }

}
