<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <form action="meals" method="get">
        <input type="hidden" name="action" value="filter">
        <table>
            <tr>
                <td>
                    <div>
                        <label for="startDate">От даты (включая)</label>
                        <input type="date" name="startDate" id="startDate" value="${param.get("startDate")}"
                               autocomplete="off">
                    </div>
                </td>
                <td>
                    <div>
                        <label for="endDate">До даты (включая)</label>
                        <input type="date" name="endDate" id="endDate" value="${param.get("endDate")}"
                               autocomplete="off">
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div>
                        <label for="startTime">От времени (включая)</label>
                        <input type="time" name="startTime" id="startTime" value="${param.get("startTime")}"
                               autocomplete="off">
                    </div>
                </td>
                <td>
                    <div>
                        <label for="endTime">До времени (исключая)</label>
                        <input type="time" name="endTime" id="endTime" value="${param.get("endTime")}"
                               autocomplete="off">
                    </div>
                </td>
            </tr>
        </table>
        <button type="submit">Filter</button>
    </form>
    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>${fn:formatDateTime(meal.dateTime)}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>