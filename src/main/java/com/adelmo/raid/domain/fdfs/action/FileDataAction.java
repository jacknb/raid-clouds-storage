package com.adelmo.raid.domain.fdfs.action;

import com.adelmo.raid.domain.fdfs.service.FileDataService;
import com.adelmo.raid.domain.fdfs.service.MonitorService;
import com.adelmo.raid.domain.fdfs.vo.Fdfs_file;
import com.adelmo.raid.domain.fdfs.vo.FileSize;
import com.adelmo.raid.domain.fdfs.vo.Group;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/dataStructure"})
public class FileDataAction {
    @Autowired
    private MonitorService monitorService;
    @Autowired
    private FileDataService fileDataService;

    public FileDataAction() {
    }

    @RequestMapping({"/fileData"})
    public ModelAndView fileData() throws IOException, MyException {
        ModelAndView mv = new ModelAndView("dataStructure/fileData.jsp");
        List list = this.monitorService.listGroups();
        HashMap container = new HashMap();

        Group group;
        FileSize sizes;
        label41:
        for(Iterator i$ = list.iterator(); i$.hasNext(); container.put(group.getGroupName(), sizes)) {
            group = (Group)i$.next();
            List files = this.fileDataService.getFileListByGroupName(group.getGroupName());
            sizes = new FileSize();
            Iterator i$1 = files.iterator();

            while(true) {
                while(true) {
                    if(!i$1.hasNext()) {
                        continue label41;
                    }

                    Fdfs_file file = (Fdfs_file)i$1.next();
                    if(file.getFileSize() >= 0L && file.getFileSize() < 30720L) {
                        sizes.setMiniSmall(sizes.getMiniSmall() + 1);
                    } else if(file.getFileSize() >= 30720L && file.getFileSize() < 102400L) {
                        sizes.setSmall(sizes.getSmall() + 1);
                    } else if(file.getFileSize() >= 101400L && file.getFileSize() < 512000L) {
                        sizes.setMiddle(sizes.getMiddle() + 1);
                    } else {
                        sizes.setLarge(sizes.getLarge() + 1);
                    }
                }
            }
        }

        mv.addObject("fileYdm", list);
        mv.addObject("fileSizes", container);
        return mv;
    }
}
