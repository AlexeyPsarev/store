<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Server error</title>
    </head>
    <body>
        <form action="home.htm" method="GET">
            <button class="btn btn-link">Home</button><br>
        </form>
        <h1>Error:</h1>
        <p>${errMsg}</p>
    </body>
</html>
