<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Manager</title>
    <button ></button>
    <h1><%= new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss").format(new java.util.Date()) %></h1>
    <form method="post">
        <input type="submit" value="Up"/>
    </form>
    <h1> ${currentPath}</h1>
    <h2>Files</h2>
    <form>${files}</form>
    <h2>Folders</h2>
    <form>${folders}</form>
</head>
<body>
</body>
</html>