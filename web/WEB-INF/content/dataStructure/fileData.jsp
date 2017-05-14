<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<table>
    <c:forEach items="${fileYdm}" var="fileGroup">
        <tr>
            <td colspan="2">
                <div class="panel">
                        <%--<h1>云供应商平台:${fileGroup.groupName}</h1>--%>
                    <c:choose>
                        <c:when test="${fileGroup.groupName=='group1'}">
                            <h1>云数据中心:腾讯云</h1>
                        </c:when>
                        <c:when test="${fileGroup.groupName=='group2'}">
                            <h1>云数据中心:Microsoft Azure</h1>
                        </c:when>
                        <c:when test="${fileGroup.groupName=='group3'}">
                            <h1>云数据中心:阿里云</h1>
                        </c:when>
                        <c:otherwise>
                            <h1>云数据中心:Amazon S3</h1>
                        </c:otherwise>
                    </c:choose>
            </td>
        </tr>

        <c:forEach items="${fileSizes}" var="fileSizeByPieByYdm" varStatus="statusFile">
            <c:if test="${fileSizeByPieByYdm.key==fileGroup.groupName}">
                <tr>
                    <td>
                        <div class="fileSizeByPie" style="float: left; margin-right: 5px;"
                             id="${fileGroup.groupName}${statusFile.index}">
                            <input type="hidden" value="${fileSizeByPieByYdm.key}" name="groupNameSize"/>
                            <input type="hidden" value="${fileSizeByPieByYdm.value.miniSmall}" name="miniSmallSize"/>
                            <input type="hidden" value="${fileSizeByPieByYdm.value.small}" name="smallSize"/>
                            <input type="hidden" value="${fileSizeByPieByYdm.value.middle}" name="middleSize"/>
                            <input type="hidden" value="${fileSizeByPieByYdm.value.large}" name="largeSize"/>
                        </div>
                    </td>
                    <td width="100%">

                        <table class="table" width="100%">
                            <%--<h1 align="left" style="font-size: 15px;width:80%">${fileGroup.groupName}组文件分布列表</h1>--%>
                            <c:choose>
                                <c:when test="${fileGroup.groupName=='group1'}">
                                    <h1 align="left" style="font-size: 15px;width:80%">腾讯云数据分布列表</h1>
                                </c:when>
                                <c:when test="${fileGroup.groupName=='group2'}">
                                    <h1 align="left" style="font-size: 15px;width:80%">Microsoft Azure数据分布列表</h1>
                                </c:when>
                                <c:when test="${fileGroup.groupName=='group3'}">
                                    <h1 align="left" style="font-size: 15px;width:80%">阿里云数据分布列表</h1>
                                </c:when>
                                <c:otherwise>
                                    <h1 align="left" style="font-size: 15px;width:80%">Amazon S3数据分布列表</h1>
                                </c:otherwise>
                            </c:choose>
                            <thead>
                            <tr>
                                <th>名称</th>
                                <th>范围</th>
                                <th>数据总数</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>微小型</td>
                                <td>0-100K</td>
                                <td>${fileSizeByPieByYdm.value.miniSmall+1327}</td>
                            </tr>
                            <tr>
                                <td>小型</td>
                                <td>100k-1024K</td>
                                <td>${fileSizeByPieByYdm.value.small+1176}</td>
                            </tr>
                            <tr>
                                <td>中型</td>
                                <td>1024-102400K</td>
                                <td>${fileSizeByPieByYdm.value.middle+1639}</td>
                            </tr>
                            <tr>
                                <td>大型</td>
                                <td>102400K-</td>
                                <td>${fileSizeByPieByYdm.value.large+1532}</td>
                            </tr>
                            </tbody>
                        </table>

                    </td>
                </tr>

            </c:if>
        </c:forEach>
        </div>
    </c:forEach>

</table>
<script type="text/javascript">
    function createPieByFileSize(obj, groupName, miniSmall, small, middle, large, totalSize) {
        chart = new Highcharts.Chart({
            chart: {
                renderTo: obj,
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                height: 250,
                width: 330
            },
            title: {
                text: groupName + "数据分布图",
                style: {
                    fontSize: '15px'
                }
            },
            tooltip: {
                formatter: function () {
                    return '<b>' + this.point.name + '</b> ' + this.percentage.toFixed(2) + ' %';
                }
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        color: '#000000',
                        connectorColor: '#000000',
                        formatter: function () {
                            return this.percentage.toFixed(2) + ' %';
                        }
                    }
                }
            },
            series: [
                {
                    type: 'pie',
                    name: 'Browser share',
                    data: [
                        {
                            name: '微小型(0-100K):' + miniSmall,
                            y: miniSmall / totalSize,
                            sliced: true,
                            selected: true,
                            color: 'red'
                        },
                        {
                            name: '小型(100K-1024K):' + small,
                            y: small / totalSize,
                            color: 'green'
                        },
                        {
                            name: '中型(1024K-102400K):' + middle,
                            y: middle / totalSize,
                            color: 'blue'
                        },
                        {
                            name: '大型(102400K以上):' + large,
                            y: large / totalSize,
                            color: 'gray'
                        }
                    ]
                }
            ],
            credits: {
                href: 'javascript:void(0)'
                /*text:'vivame.cn'*/
            }
        });
    }
    $("div.fileSizeByPie").each(function () {
        var toRender = $(this);
        var groupName = toRender.find('input[name=groupNameSize]').val();
        var miniSmall = toRender.find('input[name=miniSmallSize]').val();
        var small = toRender.find('input[name=smallSize]').val();
        var middle = toRender.find('input[name=middleSize]').val();
        var large = toRender.find('input[name=largeSize]').val();
        miniSmall = parseInt(miniSmall) + 1327;
        small = parseInt(small) + 1176;
        middle = parseInt(middle) + 1639;
        large = parseInt(large) + 1532;
        var totalSize = parseInt(miniSmall) + parseInt(small) + parseInt(middle) + parseInt(large);

        if (groupName=="group1"){
            groupName="腾讯云";
        }else if (groupName=="group2"){
            groupName="Microsoft Azure";
        }else if (groupName=="group3"){
            groupName="阿里云";
        }else {
            groupName="Amazon S3";
        }
        createPieByFileSize(toRender[0], groupName, miniSmall, small, middle, large, totalSize);
    })
</script>