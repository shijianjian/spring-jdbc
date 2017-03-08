package org.cloudfoundry.samples.music.web;

import org.cloudfoundry.samples.music.domain.DataObject;
import org.cloudfoundry.samples.music.domain.tools.ColumnRecorder;
import org.cloudfoundry.samples.music.repository.DataRepository;
import org.cloudfoundry.samples.music.tools.ConvertingTools;
import org.cloudfoundry.samples.music.tools.HttpCSVUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLDataException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping
public class MaterialController {
    private static final Logger logger = LoggerFactory.getLogger(MaterialController.class);

    @Autowired
    DataRepository repository;

    @Value("${my.default.table}")
    String table;

    @RequestMapping(method = RequestMethod.GET)
    public List<Map<String, Object>> getAll() {
        return repository.findAll(table);
    }

    @RequestMapping(value = "/columns", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> columns() {
        return ColumnRecorder.getInstance().getColumns();
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String, Object>> getMaterials(@RequestParam("query") String query){
        logger.info("Searching for '" + query + "'");
        return repository.fuzzySearch(query, null, table);
    }

    @RequestMapping(value="/album", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> add(@RequestBody String json) {
        HashMap<String, Object> map = ConvertingTools.JSONStringToHashMap(json);
        DataObject data = new DataObject(map);
        try {
            repository.insertItem(data, table);
        } catch (SQLDataException e) {
            e.printStackTrace();
        }
        logger.info("Adding object " + data.getId());
        return repository.findOne(data, table);
    }

    @RequestMapping(value="/album",method = RequestMethod.PUT)
    public Map<String, Object> update(@RequestBody String json) {
        HashMap<String, Object> map = ConvertingTools.JSONStringToHashMap(json);
        DataObject data = new DataObject(map);
        try {
            repository.updateItem(data, table);
        } catch (SQLDataException e) {
            e.printStackTrace();
        }
        logger.info("Updating object " + data.getId());
        return repository.findOne(data, table);
    }

    @RequestMapping(value = "/album/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getById(@PathVariable String id) {
        logger.info("Getting object " + id);
        return repository.findOne(id, table);
    }

    @RequestMapping(value = "/album/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String deleteById(@PathVariable String id) {
        try {
            repository.deleteItem(id, table);
        } catch (SQLDataException e) {
            e.printStackTrace();
        }
        logger.info("Deleting object " + id);
        return "Deleted";
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile multipart, RedirectAttributes redirectAttributes) {
        String res = "Success";

        HttpCSVUtils csvUtil = new HttpCSVUtils(multipart);
        String[] rows = csvUtil.getRows();
        logger.info("Import csv file: " + multipart.getOriginalFilename());
//        for(int i=0; i<rows.length; i++) {
//            String[] row = csvUtil.getSingleRowData(i);
//            if(row[csvUtil.labelIndex("_class")].endsWith("domain.Object")) {
//                Object object = new Object(
//                        row[csvUtil.labelIndex("title")],
//                        row[csvUtil.labelIndex("artist")],
//                        row[csvUtil.labelIndex("releaseYear")],
//                        row[csvUtil.labelIndex("genre")]
//                );
//                repository.save(object);
//                logger.info("Adding object: " + object.getId());
//            }
//        }
//        res += " : add " + (rows.length-1) + " albums.";

//        return res;
        return null;
    }


}