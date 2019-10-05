<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="mealList" scope="request" type="java.util.List"/>
    <jsp:useBean id="formatter" scope="request" type="java.time.format.DateTimeFormatter"/>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals List</h2>
<table>
    <tr>
        <th>Date/Time</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach var="mealTo" items="${mealList}">
        <jsp:useBean id="mealTo" type="ru.javawebinar.topjava.model.MealTo"/>
        <c:set var="style" value='${mealTo.excess == true ? "color: red" : "color: forestgreen"}'/>
        <tr style="${style}">
            <td><c:out value="${mealTo.dateTime.format(formatter)}"/></td>
            <td><c:out value="${mealTo.description}"/></td>
            <td><c:out value="${mealTo.calories}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>