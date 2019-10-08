<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="mealTo" scope="request" type="ru.javawebinar.topjava.model.MealTo"/>
    <jsp:useBean id="formatter" scope="request" type="java.time.format.DateTimeFormatter"/>
    <jsp:useBean id="id" scope="request" type="java.lang.Integer"/>
    <title>Meals</title>
</head>
<body>
<form method="post" action="meals" enctype="application/x-www-form-urlencoded">
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>Meal Edit</h2>
    <table>
        <tr>
            <th hidden>Id</th>
            <th>Date/Time</th>
            <th>Description</th>
            <th>Calories</th>
        </tr>
        <tr>
            <td hidden><input style="text-align: center"
                              type="text" name="id" value="${mealTo.id}"></td>
            <td><input style="text-align: center"
                       type="text" name="dateTime" value="${mealTo.dateTime.format(formatter)}"></td>
            <td><input style="text-align: center"
                       type="text" name="description" value="${mealTo.description}"></td>
            <td><input style="text-align: center"
                       type="text" name="calories" value="${mealTo.calories}"></td>
        </tr>
    </table>
    <br/>
    <button type="submit">save</button>
    <button type="reset">reset</button>
    <button onclick="window.history.back()">back</button>
    <br>
</form>
</body>
</html>