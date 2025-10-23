<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<body>
    <h1>Upload</h1>
    <form action="upload.action" method="post" enctype="multipart/form-data">
        <label for="file">Select a file:</label>
        <input type="file" name="upload" id="file" />
        <br><br>
        <input type="submit" value="Upload File" />
    </form>
    <br>
    <a href="listFiles.action">List Uploaded Files</a>
    <br>
    <a href="selectFiles.action">Download Multiple Files</a>
</body>
</html>