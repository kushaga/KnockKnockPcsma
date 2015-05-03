<%--
  Created by IntelliJ IDEA.
  User: suhanth
  Date: 15/4/15
  Time: 2:51 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Success</title>
</head>
<body>
<div align="center">
  <table border="0">
    <tr>
      <td colspan="2" align="center"><h2>Successful</h2></td>
    </tr>
    <tr>
      <td colspan="2" align="center">
        <h3>Event Added</h3>
      </td>
    </tr>
    <tr>
      <td>User Name:</td>
      <td>${eventForm.event_name}</td>
    </tr>

  </table>
</div>
</body>
</html>
