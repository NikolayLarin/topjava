<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <jsp:useBean id="formatter" scope="request" type="java.time.format.DateTimeFormatter"/>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals List</h2>
<table>
    <tr>
        <th hidden>Id</th>
        <th>Date/Time</th>
        <th>Description</th>
        <th>Calories</th>
        <th>Action</th>
        <th>Action</th>
    </tr>
    <c:forEach var="mealTo" items="${meals}">
        <c:set var="style" value='${mealTo.excess == true ? "color: red" : "color: forestgreen"}'/>
        <jsp:useBean id="mealTo" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr style="${style}">
            <td hidden><c:out value="${mealTo.id}"/></td>
            <td width="120"><c:out value="${mealTo.dateTime.format(formatter)}"/></td>
            <td width="50"><c:out value="${mealTo.description}"/></td>
            <td width="50"><c:out value="${mealTo.calories}"/></td>
            <td width="30"><a href="meals?id=${mealTo.id}&action=edit">edit</a></td>
            <td width="30"><a href="meals?id=${mealTo.id}&action=delete">delete</a></td>
        </tr>
    </c:forEach>
</table>
<br/>
<a href="meals?id=${-1}&action=add">add meal</a>
</body>
</html>