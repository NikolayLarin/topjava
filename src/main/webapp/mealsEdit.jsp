<%@ page import="java.time.LocalDateTime" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <jsp:useBean id="formatter" scope="request" type="java.time.format.DateTimeFormatter"/>
    <jsp:useBean id="id" scope="request" type="java.lang.Integer"/>
    <title>Meals</title>
</head>
<body>
<form method="post" action="meals" enctype="application/x-www-form-urlencoded">
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>Meals List</h2>
    <table>
        <tr>
            <th hidden>Id</th>
            <th>Date/Time</th>
            <th>Description</th>
            <th>Calories</th>
        </tr>
        <c:forEach var="mealTo" items="${meals}">
            <c:set var="style" value='${mealTo.excess == true ? "color: red" : "color: forestgreen"}'/>
            <jsp:useBean id="mealTo" type="ru.javawebinar.topjava.model.MealTo"/>
            <c:if test="${id == mealTo.id}">
                <td hidden><input style="text-align: center" type="text"
                           name="id" value="${mealTo.id}"></td>
                <td><input style="text-align: center" type="text"
                           name="dateTime" value="${mealTo.dateTime.format(formatter)}" width="120"></td>
                <td><input style="text-align: center" type="text"
                           name="description" value="${mealTo.description}" width="50"></td>
                <td><input style="text-align: center" type="text"
                           name="calories" value="${mealTo.calories}" width="50"></td>
            </c:if>
            <c:if test="${id != mealTo.id}">
                <tr style="${style}">
                    <td hidden><c:out value="${mealTo.id}"/></td>
                    <td><c:out value="${mealTo.dateTime.format(formatter)}"/></td>
                    <td><c:out value="${mealTo.description}"/></td>
                    <td><c:out value="${mealTo.calories}"/></td>
                </tr>
            </c:if>
        </c:forEach>
        <c:if test="${id == -1}">
            <td hidden><input style="text-align: center" type="text"
                       name="id" value="${id}"></td>
            <td><input style="text-align: center" type="text"
                       name="dateTime" value="<%=LocalDateTime.of(1, 1, 1, 0, 1).format(formatter)%>" width="110"></td>
            <td><input style="text-align: center" type="text"
                       name="description" value='${""}' width="50"></td>
            <td><input style="text-align: center" type="text"
                       name="calories" value="${0}" width="50"></td>
        </c:if>
    </table>
    <br/>
    <button type="submit">save</button>
    <button type="reset">reset</button>
    <button onclick="window.history.back()">back</button>
    <br>
</form>
</body>
</html>