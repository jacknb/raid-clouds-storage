package com.adelmo.raid.domain.fdfs.action;

import com.adelmo.raid.domain.fdfs.service.MonitorService;
import com.adelmo.raid.domain.fdfs.service.WarningService;
import com.adelmo.raid.domain.fdfs.vo.Forecast;
import com.adelmo.raid.domain.fdfs.vo.Group;
import com.adelmo.raid.domain.fdfs.vo.Line;
import com.adelmo.raid.domain.fdfs.vo.Storage;
import com.adelmo.raid.domain.fdfs.vo.WarningData;
import com.jcraft.jsch.JSchException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/forecast"})
public class ForecastAction {
    @Autowired
    private MonitorService monitorService;
    @Autowired
    private WarningService warningService;

    public ForecastAction() {
    }

    @RequestMapping({"/dilatation"})
    public ModelAndView dilatation() throws IOException, MyException, JSchException {
        ModelAndView mv = new ModelAndView("forecast/dilatation.jsp");
        List groupInfo = this.monitorService.listGroupInfo();
        mv.addObject("groupInfo", groupInfo);
        return mv;
    }

    @ResponseBody
    @RequestMapping({"/getDilatation"})
    public List<Line> getDilatation(String ip) throws IOException, MyException, JSchException {
        ArrayList lineList = new ArrayList();
        Line lines = new Line(ip);
        Forecast forecast = this.getForecastObject(ip);
        if(forecast.getIpAddr() != null) {
            long average = forecast.getAverage();
            Calendar timeForForecast = Calendar.getInstance();
            timeForForecast.setTime(forecast.getTimeForForecast());
            lines.getData().add(new Long[]{Long.valueOf(timeForForecast.getTimeInMillis()), Long.valueOf(forecast.getWarning() / 1024L)});

            for(int i = 0; i < 12; ++i) {
                long freeMB = (forecast.getWarning() + average * (long)(i + 1) * 24L * 30L) / 1024L;
                timeForForecast.add(2, 1);
                lines.getData().add(new Long[]{Long.valueOf(timeForForecast.getTimeInMillis()), Long.valueOf(freeMB)});
            }
        }

        lineList.add(lines);
        return lineList;
    }

    @RequestMapping({"/bottleneck"})
    public ModelAndView bottleneck() throws IOException, MyException, JSchException {
        ModelAndView mv = new ModelAndView("forecast/bottleneck.jsp");
        List groups = this.monitorService.listGroupInfo();
        mv.addObject("groups", groups);
        return mv;
    }

    @ResponseBody
    @RequestMapping({"/drawAreaAction"})
    public List<Line> drawAreaAction(String ip) throws IOException, MyException, JSchException {
        ArrayList lines = new ArrayList();
        Forecast forecast = this.getForecastObject(ip);
        Line line = new Line(ip);
        if(forecast.getIpAddr() != null) {
            Calendar c = Calendar.getInstance();
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int size = (int)(forecast.getUseHour() % 24L == 0L?forecast.getUseHour() / 24L:forecast.getUseHour() / 24L + 1L);

            for(int i = 0; i < size; ++i) {
                c.add(5, 1);
                Date date = c.getTime();
                long useMB = (forecast.getFreeMB() - 24L * forecast.getAverage() * (long)i) / 1024L;
                line.getData().add(new Object[]{date, Long.valueOf(useMB)});
            }
        }

        lines.add(line);
        return lines;
    }

    public Forecast getForecastObject(String ip) throws IOException, MyException, JSchException {
        Forecast forecast = new Forecast();
        List groupList = this.monitorService.listGroupInfo();
        Iterator i$ = groupList.iterator();

        while(i$.hasNext()) {
            Group group = (Group)i$.next();
            Iterator i$1 = group.getStorageList().iterator();

            while(i$1.hasNext()) {
                Storage storage = (Storage)i$1.next();
                if(storage.getIpAddr().equals(ip) && storage.getCurStatus().equals("ACTIVE")) {
                    long d1 = (new Date()).getTime();
                    long d2 = storage.getJoinTime().getTime();
                    long day = (d1 - d2) / 86400000L;
                    long hour = (d1 - d2) / 3600000L;
                    long hasUse = storage.getTotalMB() - storage.getFreeMB();
                    long average = hasUse / hour;
                    forecast.setAverage(average);
                    forecast.setIpAddr(storage.getIpAddr());
                    new ArrayList();
                    List warningData = this.warningService.findByIp(storage.getIpAddr());
                    long wdFreeMB = 0L;
                    if(!warningData.isEmpty()) {
                        wdFreeMB = ((WarningData)warningData.get(0)).getWdFreeMB();
                    }

                    forecast.setWarning(wdFreeMB * 1024L);
                    long mayUse = storage.getFreeMB() - wdFreeMB * 1024L;
                    long forecastHour = mayUse / average;
                    long d3 = d1 + forecastHour * 60L * 60L * 1000L;
                    Date forecastTime = new Date(d3);
                    forecast.setTimeForForecast(forecastTime);
                    forecast.setFreeMB(storage.getFreeMB());
                    forecast.setUseHour(forecastHour);
                    forecast.setNow(new Date());
                }
            }
        }

        return forecast;
    }
}
