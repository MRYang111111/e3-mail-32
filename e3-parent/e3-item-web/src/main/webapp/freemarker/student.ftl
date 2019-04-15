<html>
<head><title>学生管理freemarker</title></head>
<body>
学生信息：<br>
学生id:${student.id}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp;
学生姓名:${student.name}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp;
学生年龄:${student.age}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp;
学生住址:${student.address}&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp;<br>
学生列表：&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp;<br>
<table border="1px">
    <tr>
        <td>序号</td>
        <td>学号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>住址</td>
    </tr>
    <#list studentlist as list>
    <#--使用freemarker做出颜色判断
    ${list_index}
    取出元素的下表
    -->
    <#if list_index %2==0>
    <tr style="background: red">
    <#else>
     <tr style="background: yellow">
    </#if>
    <#--</#if>-->
        <td>${list_index}</td>
        <td>${list.id}</td>
      <td>${list.name}</td>
      <td>${list.age}</td>
      <td>${list.address}</td>
    </tr>
    </#list>
    </br>
    <#--日期的使用-->
    <#--当前时间:可以使用

    ${data?string('yyyy-MM-dd HH:mm:ss')}

    -->
    当前时间:${data?string('yyyy-MM-dd HH:mm:ss')}
    <br>
    <#--null值的处理:${val!""}-->
    null值的处理:
    <#if val??>
        该值不为null
    <#else >
        该值为nul
    </#if>
    <#--include的模板的引用-->



</table>

<#include "hello.ftl">



</body>

</html>