<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>List of Files</title>
</head>
<body>
    <h2>List of Files</h2>
    <ul>
        <s:iterator value="files">
            <li><a href="download.action?filename=<s:property/>"><s:property/></a></li>
        </s:iterator>
    </ul>
</body>
</html>