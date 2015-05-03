<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: suhanth
  Date: 15/4/15
  Time: 2:02 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Add Event</title>
</head>
<body>
<div align="center">
  <form:form action="admin" method="post" commandName="eventForm">
    <table border="0">
      <tr>
        <td colspan="2" align="center"><h2>Add Event</h2></td>
      </tr>
      <tr>
        <td>Event Name:</td>
        <td><form:input path="event_name" /></td>
      </tr>
      <tr>
        <td>Event Venue:</td>
        <td><form:input path="room_number" /></td>
      </tr>
      <tr>
        <td>Event Description:</td>
        <td><form:input path="event_description" /></td>
      </tr>
      <tr>
        <td>Event ID:</td>
        <td><form:input path="event_id" /></td>
      </tr>
      <tr>
        <td>Event Date:</td>
        <td><form:input  type="date" path="time" /></td>

      </tr>
      <tr>
        <td colspan="2" align="center"><input type="submit" value="Submit" /></td>
      </tr>
    </table>
  </form:form>
</div>
</body>
</html>
