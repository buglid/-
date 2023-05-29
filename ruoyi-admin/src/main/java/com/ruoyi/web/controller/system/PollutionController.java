package com.ruoyi.web.controller.system;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.domain.PollutionClusterDao;
import com.ruoyi.system.domain.PollutionDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Pollution;
import com.ruoyi.system.service.IPollutionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;


import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


/**
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2023-03-16
 */
@RestController
@RequestMapping("/system/pollution")
public class PollutionController extends BaseController {
    @Autowired
    private IPollutionService pollutionService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    //图聚类1,LRR
    @GetMapping("/cluster")
    public JSONObject cluster(Pollution pollution, int num) {
        startPage();
        List<Pollution> list = pollutionService.selectPollutionList(pollution);
        TableDataInfo infos = getDataTable(list);
        Map<Integer, List<Pollution>> dic = null;
        String clusterStr = "";
        double lamb;
        if (pollution.getLamb() == null)
            lamb = 0.001;
        else{
            lamb = Float.valueOf(pollution.getLamb()).floatValue();
        }

        //调用python处理数据
        try {


            Process process = Runtime.getRuntime().exec(new String[]{
                    "D:\\develop_software\\python3.9.6\\python.exe",
                    "E:\\project\\bishe\\python\\python_project2\\LRR\\LRR\\demo.py",
                    "-k " + num, "-a " + pollution.getAreaId(), "-s " + pollution.getParams().get("beginTime"), "-e " + pollution.getParams().get("endTime")+"-l "+lamb});

            InputStreamReader ir = new InputStreamReader(
                    process.getInputStream(), "UTF-8");
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            String jsonStr = "";
            boolean receiveData = false;

            while ((line = input.readLine()) != null) {
                System.out.println(line);
                if (line.contains("result")) {
                    receiveData = true;
                    jsonStr += line.replace("result", "").trim();
                } else if (receiveData) {
                    jsonStr += line;
                } else {
                    //无效数据
                }
            }
            System.out.println(jsonStr);
            input.close();
            ir.close();
            //错误日志
//            InputStreamReader ir2 = new InputStreamReader(
//                    process.getErrorStream());
//            LineNumberReader input2 = new LineNumberReader(ir2);
//            String line2;
//            String error = "";
//            while ((line2 = input2.readLine()) != null) {
//                System.out.println(line2);
//                error += line2 + "\r\n";
//            }
//            input2.close();
//            ir2.close();
            //处理数据
            if (!jsonStr.equals("")) {
                String[] arr = jsonStr.replace("[", "").replace("]", "").split(" ");
                clusterStr = String.join(",", arr);
                int len = arr.length;
                dic = new HashMap<>();
                for (int i = 0; i < num; i++) {
                    dic.put(i, new ArrayList<>());
                }
                for (int i = 0; i < arr.length; i++) {
                    String str = arr[i];
                    Pollution model = list.get(i);
                    dic.get(Integer.parseInt(str)).add(model);
                }
            }
        } catch (Exception ex) {
            String error = ex.getMessage();
        }
        System.out.println(dic);
        JSONObject object = new JSONObject();
        object.put("dic", dic);
        object.put("clusterStr", clusterStr);

//        lamb = Float.valueOf(pollution.getLamb()).floatValue();
//        System.out.println(lamb);
        return object;

    }

    //图聚类2
    @GetMapping("/SSC")
    public JSONObject SSC(Pollution pollution, int num) {
        startPage();
        List<Pollution> list = pollutionService.selectPollutionList(pollution);
        TableDataInfo infos = getDataTable(list);
        Map<Integer, List<Pollution>> SSCdic = null;
        String SSCStr = "";

        double lmbda;
        if (pollution.getLmbda() == null)
            lmbda = 0.001;
        else{
            lmbda = Float.valueOf(pollution.getLmbda()).floatValue();
        }

        //调用python处理数据
        try {
            Process process = Runtime.getRuntime().exec(new String[]{
                    "D:\\develop_software\\python3.9.6\\python.exe",
                    "E:\\project\\bishe\\python\\python_project2\\SSC\\SSC\\SSCrun.py",
                    "-k " + num, "-a " + pollution.getAreaId(), "-s " + pollution.getParams().get("beginTime"), "-e " + pollution.getParams().get("endTime")+"-l "+lmbda});
            String test = "-k " + num + "-a " + pollution.getAreaId() + "-s " + pollution.getParams().get("beginTime") + "-e " + pollution.getParams().get("endTime")+"-l "+lmbda;
            System.out.println("使用SSC");
            System.out.println(test);


            InputStreamReader ir = new InputStreamReader(
                    process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            String jsonStr = "";
            boolean receiveData = false;
            while ((line = input.readLine()) != null) {
//                System.out.println(line);
                if (line.contains("result")) {
                    receiveData = true;
                    jsonStr += line.replace("result", "").trim();
                } else if (receiveData) {
                    jsonStr += line;
                } else {
                    //无效数据
                }
            }
            System.out.println(jsonStr);
            input.close();
            ir.close();
            //错误日志
            InputStreamReader ir2 = new InputStreamReader(
                    process.getErrorStream());
            LineNumberReader input2 = new LineNumberReader(ir2);
            String line2;
            String error = "";
            while ((line2 = input2.readLine()) != null) {
//                System.out.println(line2);
                error = line2;
            }
            input2.close();
            ir2.close();
            //处理数据
            if (error.equals("") && !jsonStr.equals("")) {
                String[] arr = jsonStr.replace("[", "").replace("]", "").split(" ");
                SSCStr = String.join(",", arr);
                int len = arr.length;
                SSCdic = new HashMap<>();
                for (int i = 0; i < num; i++) {
                    SSCdic.put(i, new ArrayList<>());
                }
                for (int i = 0; i < arr.length; i++) {
                    String str = arr[i];
                    Pollution model = list.get(i);
                    SSCdic.get(Integer.parseInt(str)).add(model);
                }
            }
        } catch (Exception ex) {
            String error = ex.getMessage();
        }
        System.out.println();
        JSONObject object = new JSONObject();
        object.put("SSCdic", SSCdic);
        object.put("SSCStr", SSCStr);
        System.out.println(pollution.getLmbda());
//        lmbda=Float.valueOf(pollution.getLmbda()).floatValue();
//        System.out.println(lmbda);
        return object;

    }


    //图聚类3

    @GetMapping("/SP")
    public JSONObject SP(Pollution pollution, int num) {
        startPage();
        List<Pollution> list = pollutionService.selectPollutionList(pollution);
        TableDataInfo infos = getDataTable(list);
        Map<Integer, List<Pollution>> SPdic = null;
        String SPStr = "";
        //调用python处理数据
        try {


            Process process = Runtime.getRuntime().exec(new String[]{
                    "D:\\develop_software\\python3.9.6\\python.exe",
                    "E:\\project\\bishe\\python\\python_project2\\spectral_clustering.py",
                    "-k " + num, "-a " + pollution.getAreaId(), "-s " + pollution.getParams().get("beginTime"), "-e " + pollution.getParams().get("endTime")});

            InputStreamReader ir = new InputStreamReader(
                    process.getInputStream(), "UTF-8");
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            String jsonStr = "";
            boolean receiveData = false;

            while ((line = input.readLine()) != null) {
//                System.out.println(line);
                if (line.contains("result")) {
                    receiveData = true;
                    jsonStr += line.replace("result", "").trim();
                } else if (receiveData) {
                    jsonStr += line;
                } else {
                    //无效数据
                }
            }
            System.out.println(jsonStr);
            input.close();
            ir.close();
            //错误日志
//            InputStreamReader ir2 = new InputStreamReader(
//                    process.getErrorStream());
//            LineNumberReader input2 = new LineNumberReader(ir2);
//            String line2;
//            String error = "";
//            while ((line2 = input2.readLine()) != null) {
//                System.out.println(line2);
//                error += line2 + "\r\n";
//            }
//            input2.close();
//            ir2.close();
            //处理数据
            if (!jsonStr.equals("")) {
                String[] arr = jsonStr.replace("[", "").replace("]", "").split(" ");
                SPStr = String.join(",", arr);
                int len = arr.length;
                SPdic = new HashMap<>();
                for (int i = 0; i < num; i++) {
                    SPdic.put(i, new ArrayList<>());
                }
                for (int i = 0; i < arr.length; i++) {
                    String str = arr[i];
                    Pollution model = list.get(i);
                    SPdic.get(Integer.parseInt(str)).add(model);
                }
            }
        } catch (Exception ex) {
            String error = ex.getMessage();
        }
        System.out.println(SPdic);
        JSONObject object = new JSONObject();
        object.put("SPdic", SPdic);
        object.put("SPStr", SPStr);
        return object;
    }

    //图聚类4

    @GetMapping("/Ncut")
    public JSONObject Ncut(Pollution pollution, int num) {
        startPage();
        List<Pollution> list = pollutionService.selectPollutionList(pollution);
        TableDataInfo infos = getDataTable(list);
        Map<Integer, List<Pollution>> Ncutdic = null;
        String NcutStr = "";
        System.out.println("使用Ncut");

        //调用python处理数据
        try {
            Process process = Runtime.getRuntime().exec(new String[]{
                    "D:\\develop_software\\python3.9.6\\python.exe",
                    "E:\\project\\bishe\\python\\python_project2\\SP\\SP\\spectral\\demo.py",
                    "-k " + num, "-a " + pollution.getAreaId(), "-s " + pollution.getParams().get("beginTime"), "-e " + pollution.getParams().get("endTime")});
            String test = "-k " + num + "-a " + pollution.getAreaId() + "-s " + pollution.getParams().get("beginTime") + "-e " + pollution.getParams().get("endTime");

            System.out.println(test);


            InputStreamReader ir = new InputStreamReader(
                    process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            String jsonStr = "";
            boolean receiveData = false;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
                if (line.contains("result")) {
                    receiveData = true;
                    jsonStr += line.replace("result", "").trim();
                } else if (receiveData) {
                    jsonStr += line;
                } else {
                    //无效数据
                    System.out.println("无效数据");
                }
            }
//            System.out.println(jsonStr);
//            System.out.println("---------------------------------");
            input.close();
            ir.close();
            //错误日志
//            InputStreamReader ir2 = new InputStreamReader(
//                    process.getErrorStream());
//            LineNumberReader input2 = new LineNumberReader(ir2);
//            String line2;
//            String error = "";
//            while ((line2 = input2.readLine()) != null) {
//                System.out.println(line2);
//                error = line2;
//            }
//            input2.close();
//            ir2.close();
            //处理数据
            if (!jsonStr.equals("")) {
                String[] arr = jsonStr.replace("[", "").replace("]", "").split(" ");
                NcutStr = String.join(",", arr);
                int len = arr.length;
                Ncutdic = new HashMap<>();
                for (int i = 0; i < num; i++) {
                    Ncutdic.put(i, new ArrayList<>());
                }
                for (int i = 0; i < arr.length; i++) {
                    String str = arr[i];
                    Pollution model = list.get(i);
                    Ncutdic.get(Integer.parseInt(str)).add(model);
                }
            }
        } catch (Exception ex) {
//            String error = ex.getMessage();
        }
//        System.out.println(Ncutdic);
        JSONObject object = new JSONObject();
        object.put("Ncutdic", Ncutdic);
        object.put("NcutStr", NcutStr);
        return object;

    }


    //图聚类5
    @GetMapping("/Rcut")
    public JSONObject Rcut(Pollution pollution, int num) {
        startPage();
        List<Pollution> list = pollutionService.selectPollutionList(pollution);
        TableDataInfo infos = getDataTable(list);
        Map<Integer, List<Pollution>> Rcutdic = null;
        String RcutStr = "";

        //调用python处理数据
        try {
            Process process = Runtime.getRuntime().exec(new String[]{
                    "D:\\develop_software\\python3.9.6\\python.exe",
                    "E:\\project\\bishe\\python\\python_project2\\Rcut\\Rcut\\run.py",
                    "-k " + num, "-a " + pollution.getAreaId(), "-s " + pollution.getParams().get("beginTime"), "-e " + pollution.getParams().get("endTime")});
            String test = "-k " + num + "-a " + pollution.getAreaId() + "-s " + pollution.getParams().get("beginTime") + "-e " + pollution.getParams().get("endTime");
            System.out.println("使用Rcut");
            System.out.println(test);


            InputStreamReader ir = new InputStreamReader(
                    process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            String jsonStr = "";
            boolean receiveData = false;
            while ((line = input.readLine()) != null) {
//                System.out.println(line);
                if (line.contains("result")) {
                    receiveData = true;
                    jsonStr += line.replace("result", "").trim();
                } else if (receiveData) {
                    jsonStr += line;
                } else {
                    //无效数据
                }
            }
            System.out.println(jsonStr);
            input.close();
            ir.close();
            //错误日志
//            InputStreamReader ir2 = new InputStreamReader(
//                    process.getErrorStream());
//            LineNumberReader input2 = new LineNumberReader(ir2);
//            String line2;
//            String error = "";
//            while ((line2 = input2.readLine()) != null) {
//                System.out.println(line2);
//                error = line2;
//            }
//            input2.close();
//            ir2.close();
            //处理数据
            if (!jsonStr.equals("")) {
                String[] arr = jsonStr.replace("[", "").replace("]", "").split(" ");
                RcutStr = String.join(",", arr);
                int len = arr.length;
                Rcutdic = new HashMap<>();
                for (int i = 0; i < num; i++) {
                    Rcutdic.put(i, new ArrayList<>());
                }
                for (int i = 0; i < arr.length; i++) {
                    String str = arr[i];
                    Pollution model = list.get(i);
                    Rcutdic.get(Integer.parseInt(str)).add(model);
                }
            }
        } catch (Exception ex) {
            String error = ex.getMessage();
        }
        JSONObject object = new JSONObject();
        object.put("Rcutdic", Rcutdic);
        object.put("RcutStr", RcutStr);

        return object;

    }


    //密度聚类1
    @GetMapping("/DBSCAN")
    public JSONObject DBSCAN(Pollution pollution, int num) {
        startPage();
        List<Pollution> list = pollutionService.selectPollutionList(pollution);
        TableDataInfo infos = getDataTable(list);
        Map<Integer, List<Pollution>> DBSCANdic = null;
        String DBSCANStr = "";

        double eps;
        if (pollution.getEps() == null)
            eps = 19.9;
        else{
            eps = Float.valueOf(pollution.getEps()).floatValue();
        }

        int min_samples;
        if (pollution.getMin_samples() == null)
            min_samples = 12;
        else{
            min_samples = Integer.valueOf(pollution.getMin_samples()).intValue();
        }


//        System.out.println("使用Ncut");
        System.out.println("使用DBSCAN");

        //调用python处理数据
        try {
            Process process = Runtime.getRuntime().exec(new String[]{
                    "D:\\develop_software\\python3.9.6\\python.exe",
                    "E:\\project\\bishe\\python\\suanfa\\suanfa\\dbscan\\sklearn_Dbscan.py",
                    "-k " + num, "-a " + pollution.getAreaId(), "-s " + pollution.getParams().get("beginTime"), "-e " + pollution.getParams().get("endTime")});
            String test = "-k " + num + "-a " + pollution.getAreaId() + "-s " + pollution.getParams().get("beginTime") + "-e " + pollution.getParams().get("endTime")+"-eps "+eps+"-m "+min_samples;

            System.out.println(test);

            InputStreamReader ir = new InputStreamReader(
                    process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            String jsonStr = "";
            boolean receiveData = false;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
                if (line.contains("result")) {
                    receiveData = true;
                    jsonStr += line.replace("result", "").trim();
                } else if (receiveData) {
                    jsonStr += line;
                } else {
                    //无效数据
                    System.out.println("无效数据");
                }
            }
            input.close();
            ir.close();

            //处理数据
            if (!jsonStr.equals("")) {
                String[] arr = jsonStr.replace("[", "").replace("]", "").split(" ");
                DBSCANStr = String.join(",", arr);
                int len = arr.length;
                DBSCANdic = new HashMap<>();
                for (int i = 0; i < num; i++) {
                    DBSCANdic.put(i, new ArrayList<>());
                }
                for (int i = 0; i < arr.length; i++) {
                    String str = arr[i];
                    Pollution model = list.get(i);
                    if(str.equals("-1")){
                        continue;
                    }
                    DBSCANdic.get(Integer.parseInt(str)).add(model);
                }
            }
        } catch (Exception ex) {
            String error = ex.getMessage();
        }
        System.out.println(DBSCANdic);
        JSONObject object = new JSONObject();
        object.put("DBSCANdic", DBSCANdic);
        object.put("DBSCANStr", DBSCANStr);

        System.out.println(eps);
        System.out.println(min_samples);

        System.out.println(object);
        return object;

    }

    //密度聚类2
    @GetMapping("/KMeans")
    public JSONObject KMeans(Pollution pollution, int num) {
        startPage();
        List<Pollution> list = pollutionService.selectPollutionList(pollution);
        TableDataInfo infos = getDataTable(list);
        Map<Integer, List<Pollution>> KMeansdic = null;
        String KMeansStr = "";
        System.out.println("使用KMeans");

        //调用python处理数据
        try {
            Process process = Runtime.getRuntime().exec(new String[]{
                    "D:\\develop_software\\python3.9.6\\python.exe",
                    "E:\\project\\bishe\\python\\suanfa\\suanfa\\kmeans\\main2.py",
                    "-k " + num, "-a " + pollution.getAreaId(), "-s " + pollution.getParams().get("beginTime"), "-e " + pollution.getParams().get("endTime")});
            String test = "-k " + num + "-a " + pollution.getAreaId() + "-s " + pollution.getParams().get("beginTime") + "-e " + pollution.getParams().get("endTime");

            System.out.println(test);


            InputStreamReader ir = new InputStreamReader(
                    process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            String jsonStr = "";
            boolean receiveData = false;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
                if (line.contains("result")) {
                    receiveData = true;
                    jsonStr += line.replace("result", "").trim();
                } else if (receiveData) {
                    jsonStr += line;
                } else {
                    //无效数据
                    System.out.println("无效数据");
                }
            }
            input.close();
            ir.close();

            //处理数据
            if (!jsonStr.equals("")) {
                String[] arr = jsonStr.replace("[", "").replace("]", "").split(" ");
                KMeansStr = String.join(",", arr);
                int len = arr.length;
                KMeansdic = new HashMap<>();
                for (int i = 0; i < num; i++) {
                    KMeansdic.put(i, new ArrayList<>());
                }
                for (int i = 0; i < arr.length; i++) {
                    String str = arr[i];
                    Pollution model = list.get(i);
                    KMeansdic.get(Integer.parseInt(str)).add(model);
                }
            }
        } catch (Exception ex) {
//            String error = ex.getMessage();
        }
//        System.out.println(KMeansdic);
        JSONObject object = new JSONObject();
        object.put("KMeansdic", KMeansdic);
        object.put("KMeansStr", KMeansStr);
        return object;

    }

    //密度聚类3
    @GetMapping("/DP")
    public JSONObject DP(Pollution pollution, int num) {
        startPage();
        List<Pollution> list = pollutionService.selectPollutionList(pollution);
        TableDataInfo infos = getDataTable(list);
        Map<Integer, List<Pollution>> DPdic = null;
        String DPStr = "";
        System.out.println("使用DP");

        //调用python处理数据
        try {
            Process process = Runtime.getRuntime().exec(new String[]{
                    "D:\\develop_software\\python3.9.6\\python.exe",
                    "E:\\project\\bishe\\python\\suanfa\\suanfa\\dp\\DP.py",
                    "-k " + num, "-a " + pollution.getAreaId(), "-s " + pollution.getParams().get("beginTime"), "-e " + pollution.getParams().get("endTime")});
            String test = "-k " + num + "-a " + pollution.getAreaId() + "-s " + pollution.getParams().get("beginTime") + "-e " + pollution.getParams().get("endTime");

            System.out.println(test);


            InputStreamReader ir = new InputStreamReader(
                    process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            String jsonStr = "";
            boolean receiveData = false;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
                if (line.contains("result")) {
                    receiveData = true;
                    jsonStr += line.replace("result", "").trim();
                } else if (receiveData) {
                    jsonStr += line;
                } else {
                    //无效数据
                    System.out.println("无效数据");
                }
            }
            input.close();
            ir.close();

            //处理数据
            if (!jsonStr.equals("")) {
                String[] arr = jsonStr.replace("[", "").replace("]", "").split(" ");
                DPStr = String.join(",", arr);
                int len = arr.length;
                DPdic = new HashMap<>();
                for (int i = 0; i < num; i++) {
                    DPdic.put(i, new ArrayList<>());
                }
                for (int i = 0; i < arr.length; i++) {
                    String str = arr[i];
                    Pollution model = list.get(i);
                    DPdic.get(Integer.parseInt(str)).add(model);
                }
            }
        } catch (Exception ex) {
//            String error = ex.getMessage();
        }
//        System.out.println(DPdic);
        JSONObject object = new JSONObject();
        object.put("DPdic", DPdic);
        object.put("DPStr", DPStr);
        return object;

    }

    //密度聚类4
    @GetMapping("/CLUB")
    public JSONObject CLUB(Pollution pollution, int num) {
        startPage();
        List<Pollution> list = pollutionService.selectPollutionList(pollution);
        TableDataInfo infos = getDataTable(list);
        Map<Integer, List<Pollution>> CLUBdic = null;
        String CLUBStr = "";
        System.out.println("使用CLUB");

        //调用python处理数据
        try {
            Process process = Runtime.getRuntime().exec(new String[]{
                    "D:\\develop_software\\python3.9.6\\python.exe",
                    "E:\\project\\bishe\\python\\suanfa\\suanfa\\club\\main.py",
                    "-k " + num, "-a " + pollution.getAreaId(), "-s " + pollution.getParams().get("beginTime"), "-e " + pollution.getParams().get("endTime")});
            String test = "-k " + num + "-a " + pollution.getAreaId() + "-s " + pollution.getParams().get("beginTime") + "-e " + pollution.getParams().get("endTime");

            System.out.println(test);


            InputStreamReader ir = new InputStreamReader(
                    process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            String jsonStr = "";
            boolean receiveData = false;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
                if (line.contains("result")) {
                    receiveData = true;
                    jsonStr += line.replace("result", "").trim();
                } else if (receiveData) {
                    jsonStr += line;
                } else {
                    //无效数据
                    System.out.println("无效数据");
                }
            }
            input.close();
            ir.close();

            //处理数据
            if (!jsonStr.equals("")) {
                String[] arr = jsonStr.replace("[", "").replace("]", "").split(" ");
                CLUBStr = String.join(",", arr);
                int len = arr.length;
                CLUBdic = new HashMap<>();
                for (int i = 0; i < num; i++) {
                    CLUBdic.put(i, new ArrayList<>());
                }
                for (int i = 0; i < arr.length; i++) {
                    String str = arr[i];
                    Pollution model = list.get(i);
                    CLUBdic.get(Integer.parseInt(str)).add(model);
                }
            }
        } catch (Exception ex) {
//            String error = ex.getMessage();
        }
//        System.out.println(CLUBdic);
        JSONObject object = new JSONObject();
        object.put("CLUBdic", CLUBdic);
        object.put("CLUBStr", CLUBStr);
        return object;

    }

    //密度聚类5
    @GetMapping("/EPC")
    public JSONObject EPC(Pollution pollution, int num) {
        startPage();
        List<Pollution> list = pollutionService.selectPollutionList(pollution);
        TableDataInfo infos = getDataTable(list);
        Map<Integer, List<Pollution>> EPCdic = null;
        String EPCStr = "";
        System.out.println("使用EPC");

        //调用python处理数据
        try {
            Process process = Runtime.getRuntime().exec(new String[]{
                    "D:\\develop_software\\python3.9.6\\python.exe",
                    "E:\\project\\bishe\\python\\suanfa\\suanfa\\epc-cluster\\EPC_cluster.py",
                    "-k " + num, "-a " + pollution.getAreaId(), "-s " + pollution.getParams().get("beginTime"), "-e " + pollution.getParams().get("endTime")});
            String test = "-k " + num + "-a " + pollution.getAreaId() + "-s " + pollution.getParams().get("beginTime") + "-e " + pollution.getParams().get("endTime");

            System.out.println(test);


            InputStreamReader ir = new InputStreamReader(
                    process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            String jsonStr = "";
            boolean receiveData = false;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
                if (line.contains("result")) {
                    receiveData = true;
                    jsonStr += line.replace("result", "").trim();
                } else if (receiveData) {
                    jsonStr += line;
                } else {
                    //无效数据
                    System.out.println("无效数据");
                }
            }
            input.close();
            ir.close();

            //处理数据
            if (!jsonStr.equals("")) {
                String[] arr = jsonStr.replace("[", "").replace("]", "").split(" ");
                EPCStr = String.join(",", arr);
                int len = arr.length;
                EPCdic = new HashMap<>();
                for (int i = 0; i < num; i++) {
                    EPCdic.put(i, new ArrayList<>());
                }
                for (int i = 0; i < arr.length; i++) {
                    String str = arr[i];
                    Pollution model = list.get(i);
                    EPCdic.get(Integer.parseInt(str)).add(model);
                }
            }
        } catch (Exception ex) {
//            String error = ex.getMessage();
        }
//        System.out.println(EPCdic);
        JSONObject object = new JSONObject();
        object.put("EPCdic", EPCdic);
        object.put("EPCStr", EPCStr);
        return object;

    }


    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<Pollution> util = new ExcelUtil<Pollution>(Pollution.class);
        util.importTemplateExcel(response, "环境数据");
    }


    @Log(title = "环境数据管理", businessType = BusinessType.IMPORT)
//    @PreAuthorize("@ss.hasPermi('system:pollution:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<Pollution> util = new ExcelUtil<Pollution>(Pollution.class);
        List<Pollution> pollutionList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = pollutionService.importPillution(pollutionList, updateSupport, operName);
        return success(message);
    }


    @GetMapping("/craw")
    public Map<Integer, List<Pollution>> craw(String areaId, String webSite, String cityName, String startTime, String endTime) {
        startPage();
        Map<Integer, List<Pollution>> dic = null;
        //调用python处理数据
        try {
            Date day = new Date();
            String enterDate = sdf.format(day);
            cityName = toPinyin(cityName);
            cityName = cityName.substring(0, cityName.length() - 3);
            Process process = Runtime.getRuntime().exec(new String[]{
                    "D:\\develop_software\\python3.9.6\\python.exe",
                    "E:\\project\\bishe\\python\\pythonProject2\\test1\\paqu.py",
//                    "-c 杭州", "-s 2020-04-07", "-e 2020-05-01", "-a 3301", "-d 2023-04-03", "-u 1"});
                    "-c " + cityName, "-s " + startTime, "-e " + endTime, "-a " + areaId, "-d " + enterDate, "-u " + getUserId()});

            String str = "-c " + cityName + " -s " + startTime + " -et " + endTime + " -a " + areaId + " -ed " + enterDate + " -u " + getUserId();
            System.out.println(str);

            InputStreamReader ir = new InputStreamReader(
                    process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line;
            String jsonStr = "";
            boolean receiveData = false;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
                if (line.contains("result")) {
                    receiveData = true;
                    jsonStr += line.replace("result", "").trim();
                } else if (receiveData) {
                    jsonStr += line;
                } else {
                    //无效数据
                }
            }
            System.out.println(jsonStr);
            input.close();
            ir.close();
            //错误日志
            InputStreamReader ir2 = new InputStreamReader(
                    process.getErrorStream());
            LineNumberReader input2 = new LineNumberReader(ir2);
            String line2;
            String error = "";
            while ((line2 = input2.readLine()) != null) {
                System.out.println(line2);
                error = line2;
            }
            input2.close();
            ir2.close();
            //处理数据
        } catch (Exception ex) {
            String error = ex.getMessage();
        }
        return dic;
    }


    /**
     * 汉字转为拼音
     *
     * @param chinese
     * @return
     */
    public static String toPinyin(String chinese) {
        String pinyinStr = "";
        char[] newChar = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < newChar.length; i++) {
            if (newChar[i] > 128) {
                try {
                    pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0];
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pinyinStr += newChar[i];
            }
        }
        return pinyinStr;
    }


    /**
     * 查询【请填写功能名称】列表
     */
//    @PreAuthorize("@ss.hasPermi('system:pollution:list')")
    @GetMapping("/list")
    public TableDataInfo list(Pollution pollution) {
        startPage();
        List<Pollution> list = pollutionService.selectPollutionList(pollution);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
//    @PreAuthorize("@ss.hasPermi('system:pollution:export')")
    @Log(title = "环境数据导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Pollution pollution) {
        startPage();
        List<Pollution> list = pollutionService.selectPollutionList(pollution);
        ExcelUtil<Pollution> util = new ExcelUtil<Pollution>(Pollution.class);
        util.exportExcel(response, list, "环境数据");
    }

    /**
     * 导出【请填写功能名称】列表
     */
//    @PreAuthorize("@ss.hasPermi('system:pollution:export')")
    @Log(title = "环境数据导出", businessType = BusinessType.EXPORT)
    @PostMapping("/exportstandardization")
    public void exportStandardization(HttpServletResponse response, Pollution pollution, int type) {
        startPage();
        List<Pollution> list = pollutionService.selectPollutionList(pollution);
        List<PollutionDao> list2 = new ArrayList<>();
        if (type == 1) {
            list = calZScore(list);
        } else {
            list = calMinMax(list);
        }
        for (Pollution pollution1 : list) {
            PollutionDao dao = new PollutionDao();
            dao.setAreaId(pollution1.getAreaId());
            dao.setId(pollution1.getId());
            dao.setDate(pollution1.getDate());
            dao.setEnterDate(pollution1.getEnterDate());
            dao.setUserId(pollution1.getUserId());
            dao.setRemarks(pollution1.getRemarks());
            dao.setPM10((String) pollution1.getParams().get("pm10"));
            dao.setCO((String) pollution1.getParams().get("co"));
            dao.setPm25((String) pollution1.getParams().get("pm25"));
            dao.setNO2((String) pollution1.getParams().get("no2"));
            dao.setSO2((String) pollution1.getParams().get("so2"));
            dao.setO3((String) pollution1.getParams().get("o3"));
            list2.add(dao);
        }
        ExcelUtil<PollutionDao> util = new ExcelUtil<PollutionDao>(PollutionDao.class);
        util.exportExcel(response, list2, "环境数据");
    }


    /**
     * 导出【请填写功能名称】列表
     */
//    @PreAuthorize("@ss.hasPermi('system:pollution:export')")
    @Log(title = "导出聚类数据", businessType = BusinessType.EXPORT)
    @PostMapping("/exportcluster")
    public void exportCluster(HttpServletResponse response, Pollution pollution, String clusterstr) {
        startPage();
        List<Pollution> list = pollutionService.selectPollutionList(pollution);
        List<PollutionClusterDao> list2 = new ArrayList<>();
        String[] arr = null;
        if (clusterstr != null && !clusterstr.equals("")) {
            arr = clusterstr.split(",");
        }
        for (int i = 0; i < list.size(); i++) {
            Pollution pollution1 = list.get(i);
            PollutionClusterDao dao = new PollutionClusterDao();
            dao.setAreaId(pollution1.getAreaId());
            dao.setId(pollution1.getId());
            dao.setDate(pollution1.getDate());
            dao.setEnterDate(pollution1.getEnterDate());
            dao.setUserId(pollution1.getUserId());
            dao.setRemarks(pollution1.getRemarks());
            dao.setPM10(pollution1.getPM10() + "");
            dao.setCO(pollution1.getCO() + "");
            dao.setPm25(pollution1.getPm25() + "");
            dao.setNO2(pollution1.getNO2() + "");
            dao.setSO2(pollution1.getSO2() + "");
            dao.setO3(pollution1.getO3() + "");
            dao.setBelong(arr == null ? "" : arr[i]);
            list2.add(dao);
        }
        ExcelUtil<PollutionClusterDao> util = new ExcelUtil<PollutionClusterDao>(PollutionClusterDao.class);
        util.exportExcel(response, list2, "环境数据");
    }


    /**
     * 获取【请填写功能名称】详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:pollution:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(pollutionService.selectPollutionById(id));
    }

    /**
     * 新增【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:pollution:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Pollution pollution) {
        pollution.setEnterDate(new Date());
        return toAjax(pollutionService.insertPollution(pollution));
    }

    /**
     * 修改【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:pollution:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Pollution pollution) {
        return toAjax(pollutionService.updatePollution(pollution));
    }

    /**
     * 删除【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:pollution:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(pollutionService.deletePollutionByIds(ids));
    }


    /**
     * 标准化
     */
    @GetMapping("/standardization")
    public TableDataInfo standardization(Pollution pollution, int type) {
        startPage();
        List<Pollution> list = pollutionService.selectPollutionList(pollution);
        if (type == 1) {//Z-Score标准化
            list = calZScore(list);
        } else {//0-1标准化
            list = calMinMax(list);
        }
        return getDataTable(list);
    }

    private List<Pollution> calMinMax(List<Pollution> list) {
        long maxPm25 = -99999;
        long minPm25 = 99999;
        long maxPm10 = -99999;
        long minPm10 = 99999;
        long maxSo2 = -99999;
        long minSo2 = 99999;
        long maxNo2 = -99999;
        long minNo2 = 99999;
        double maxCo = -99999;
        double minCo = 99999;
        long maxO3 = -99999;
        long minO3 = 99999;
        for (Pollution pollution : list) {
            maxPm25 = Long.parseLong(pollution.getPm25()) > maxPm25 ? Long.parseLong(pollution.getPm25()) : maxPm25;
            minPm25 = Long.parseLong(pollution.getPm25()) < minPm25 ? Long.parseLong(pollution.getPm25()) : minPm25;

            maxPm10 = Long.parseLong(pollution.getPM10()) > maxPm10 ? Long.parseLong(pollution.getPM10()) : maxPm10;
            minPm10 = Long.parseLong(pollution.getPM10()) < minPm10 ? Long.parseLong(pollution.getPM10()) : minPm10;

            maxSo2 = Long.parseLong(pollution.getSO2()) > maxSo2 ? Long.parseLong(pollution.getSO2()) : maxSo2;
            minSo2 = Long.parseLong(pollution.getSO2()) < minSo2 ? Long.parseLong(pollution.getSO2()) : minSo2;

            maxNo2 = Long.parseLong(pollution.getNO2()) > maxNo2 ? Long.parseLong(pollution.getNO2()) : maxNo2;
            minNo2 = Long.parseLong(pollution.getNO2()) < minNo2 ? Long.parseLong(pollution.getNO2()) : minNo2;

            maxCo = Float.parseFloat(pollution.getCO()) > maxCo ? Float.parseFloat(pollution.getCO()) : maxCo;
            minCo = Float.parseFloat(pollution.getCO()) < minCo ? Float.parseFloat(pollution.getCO()) : minCo;

            maxO3 = Long.parseLong(pollution.getO3()) > maxO3 ?Long.parseLong(pollution.getO3()) : maxO3;
            minO3 = Long.parseLong(pollution.getO3()) < minO3 ? Long.parseLong(pollution.getO3()) : minO3;
        }

        for (Pollution pollution : list) {
            String pm25 = "";
            String pm10 = "";
            String so2 = "";
            String no2 = "";
            String co = "";
            String o3 = "";
            if ((maxPm25 - minPm25) != 0) {
                pm25 = new Formatter().format("%.2f", (Long.parseLong(pollution.getPm25()) - minPm25) * 1.0 / (maxPm25 - minPm25)).toString();
            }
            if ((maxPm10 - minPm10) != 0) {
                pm10 = new Formatter().format("%.2f", (Long.parseLong(pollution.getPM10()) - minPm10) * 1.0 / (maxPm10 - minPm10)).toString();
            }
            if ((maxSo2 - minSo2) != 0) {
                so2 = new Formatter().format("%.2f", (Long.parseLong(pollution.getSO2()) - minSo2) * 1.0 / (maxSo2 - minSo2)).toString();
            }
            if ((maxNo2 - minNo2) != 0) {
                no2 = new Formatter().format("%.2f", (Long.parseLong(pollution.getNO2()) - minNo2) * 1.0 / (maxNo2 - minNo2)).toString();
            }
            if ((maxCo - minCo) != 0) {
                co = new Formatter().format("%.2f", (Float.parseFloat(pollution.getCO()) - minCo) * 1.0 / (maxCo - minCo)).toString();
            }
            if ((maxO3 - minO3) != 0) {
                o3 = new Formatter().format("%.2f", (Long.parseLong(pollution.getO3()) - minO3) * 1.0 / (maxO3 - minO3)).toString();
            }
            Map<String, Object> map = new HashMap<>();
            map.put("pm25", pm25);
            map.put("pm10", pm10);
            map.put("so2", so2);
            map.put("no2", no2);
            map.put("co", co);
            map.put("o3", o3);
            pollution.setParams(map);
        }
        return list;
    }

    private List<Pollution> calZScore(List<Pollution> list) {
        /**
         * 计算平均数
         */
        long totalPm25 = 0;
        long totalPm10 = 0;
        long totalSo2 = 0;
        long totalNo2 = 0;
        long totalCo = 0;
        long totalO3 = 0;
        for (Pollution pollution : list) {
            totalPm25 += Long.parseLong(pollution.getPm25());
            totalPm10 += Long.parseLong(pollution.getPM10());
            totalSo2 += Long.parseLong(pollution.getSO2());
            totalNo2 += Long.parseLong(pollution.getNO2());
            totalCo += Float.parseFloat(pollution.getCO());
            totalO3 += Long.parseLong(pollution.getO3());
        }
        /**
         *  计算平均数
         */
        double averagePm25 = 0;
        double averagePm10 = 0;
        double averageSo2 = 0;
        double averageNo2 = 0;
        double averageCo = 0;
        double averageO3 = 0;
        if (list.size() > 0) {
            averagePm25 = Double.parseDouble(new Formatter().format("%.2f", totalPm25 * 1.0 / list.size()).toString());
            averagePm10 = Double.parseDouble(new Formatter().format("%.2f", totalPm10 * 1.0 / list.size()).toString());
            averageSo2 = Double.parseDouble(new Formatter().format("%.2f", totalSo2 * 1.0 / list.size()).toString());
            averageNo2 = Double.parseDouble(new Formatter().format("%.2f", totalNo2 * 1.0 / list.size()).toString());
            averageCo = Double.parseDouble(new Formatter().format("%.2f", totalCo * 1.0 / list.size()).toString());
            averageO3 = Double.parseDouble(new Formatter().format("%.2f", totalO3 * 1.0 / list.size()).toString());
        }
        /**
         * 计算标准差
         */
        double tempPM25 = 0;
        double tempPm10 = 0;
        double tempSo2 = 0;
        double tempNo2 = 0;
        double tempCo = 0;
        double tempO3 = 0;
        for (Pollution pollution : list) {
            tempPM25 += Math.pow((Double.parseDouble(pollution.getPm25()) - averagePm25), 2);
            tempPm10 += Math.pow((Double.parseDouble(pollution.getPM10()) - averagePm10), 2);
            tempSo2 += Math.pow((Double.parseDouble(pollution.getSO2()) - averageSo2), 2);
            tempNo2 += Math.pow((Double.parseDouble(pollution.getNO2() )- averageNo2), 2);
            tempCo += Math.pow((Double.parseDouble(pollution.getCO()) - averageCo), 2);
            tempO3 += Math.pow((Double.parseDouble(pollution.getO3()) - averageO3), 2);
        }
        double standardDeviationPM25 = 0;
        double standardDeviationPm10 = 0;
        double standardDeviationSo2 = 0;
        double standardDeviationNo2 = 0;
        double standardDeviationCo = 0;
        double standardDeviationO3 = 0;
        if (list.size() > 0) {
            standardDeviationPM25 = Double.parseDouble(new Formatter().format("%.2f", Math.sqrt(tempPM25 * 1.0 / list.size())).toString());
            standardDeviationPm10 = Double.parseDouble(new Formatter().format("%.2f", Math.sqrt(tempPm10 * 1.0 / list.size())).toString());
            standardDeviationSo2 = Double.parseDouble(new Formatter().format("%.2f", Math.sqrt(tempSo2 * 1.0 / list.size())).toString());
            standardDeviationNo2 = Double.parseDouble(new Formatter().format("%.2f", Math.sqrt(tempNo2 * 1.0 / list.size())).toString());
            standardDeviationCo = Double.parseDouble(new Formatter().format("%.2f", Math.sqrt(tempCo * 1.0 / list.size())).toString());
            standardDeviationO3 = Double.parseDouble(new Formatter().format("%.2f", Math.sqrt(tempO3 * 1.0 / list.size())).toString());
        }
        for (Pollution pollution : list) {
            Map<String, Object> map = new HashMap<>();
            String pm25 = "";
            String pm10 = "";
            String so2 = "";
            String no2 = "";
            String co = "";
            String o3 = "";
            if (standardDeviationPM25 != 0) {
                pm25 = new Formatter().format("%.2f", (Double.parseDouble(pollution.getPm25()) - averagePm25) * 1.0 / standardDeviationPM25).toString();
            }
            if (standardDeviationPm10 != 0) {
                pm10 = new Formatter().format("%.2f", (Double.parseDouble(pollution.getPM10()) - averagePm10) * 1.0 / standardDeviationPm10).toString();
            }
            if (standardDeviationSo2 != 0) {
                so2 = new Formatter().format("%.2f", (Double.parseDouble(pollution.getSO2()) - averageSo2) * 1.0 / standardDeviationSo2).toString();
            }
            if (standardDeviationNo2 != 0) {
                no2 = new Formatter().format("%.2f", (Double.parseDouble(pollution.getNO2()) - averageNo2) * 1.0 / standardDeviationNo2).toString();
            }
            if (standardDeviationCo != 0) {
                co = new Formatter().format("%.2f", (Double.parseDouble(pollution.getCO()) - averageCo) * 1.0 / standardDeviationCo).toString();
            }
            if (standardDeviationO3 != 0) {
                o3 = new Formatter().format("%.2f", (Double.parseDouble(pollution.getO3()) - averageO3) * 1.0 / standardDeviationO3).toString();
            }
            map.put("pm25", pm25);
            map.put("pm10", pm10);
            map.put("so2", so2);
            map.put("no2", no2);
            map.put("co", co);
            map.put("o3", o3);
            pollution.setParams(map);
        }
        return list;
    }
}
