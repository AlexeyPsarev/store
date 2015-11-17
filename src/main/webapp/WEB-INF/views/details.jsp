<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<html>
    <head>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="js/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
        <link href="js/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
        <title>Details</title>
    </head>
    <body>
        <!-- /home.htm?category=${app.category}&page=1 -->
        <button class="btn-link">Home</button>
        <!-- common -->
        <div>
            <p>Most popular</p>
            <div style="min-height: 128px">
                <c:forEach var="item" items="${popularApps}">
                    <img src="${item.pic128}" height="128" width="128" style="margin-left: 10px">
                </c:forEach>
            </div>
        </div>
        <hr>
        <div style="margin-top: 10px">
            <img src="${app.pic512}" height="512" width="512" style="margin-left: 10px; margin-right: 50px; float: left">
            <div class="container" style="min-height: 512px">
                <div style="min-height: 460px">
                    <p><b>${app.appName}</b></p>
                    <br>
                    <p><b>Description:</b></p>
                    <p style="white-space: pre-wrap"><c:out value="${app.description}"/></p>
                    <br>
                    <p><b>Author:</b></p>
                    <p>${author.fullname}</p>
                </div>
                <form action="download.htm" method="GET">
                    <input type="hidden" name="app" value="${app.id}">
                    <button class="btn btn-success">Download</button>
                </form>
            </div>
        </div>
    </body>
</html>
