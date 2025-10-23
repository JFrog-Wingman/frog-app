<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>List of Files</title>
</head>
<body>
    <h2>List of Files</h2>
    <form action="downloads.action" method="post">
        <ul>
            <s:iterator value="files">
                <li><input type="checkbox" name="filenames" value="<s:property/>"/><s:property/></li>
            </s:iterator>
        </ul>
        <input type="submit" value="Download Selected Files"/>
    </form>
</body>
</html>