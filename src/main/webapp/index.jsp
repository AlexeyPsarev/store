<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>
    <head>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="js/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
        <link href="js/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
        <style type="text/css">
            html, body, div {
                height: 100%;
            }
            .row {
                margin-left: 0px;
                margin-right: 0px;
            }
        </style>
        <title></title>
    </head>
    <body>
        <div class="row" style="height: 100%">
            <div class="col-lg-5"></div>
            <div class="col-lg-2" style="display: table">
                <div style="display: table-cell; vertical-align: middle">
                <form action="loginForm.htm" method="GET">
                    <input type="submit" class="btn btn-primary btn-block" value="Sign in">
                </form>
                <form action="registrationForm.htm" method="GET">
                    <input type="submit" class="btn btn-primary btn-block" style="margin-top: 10px" value="Register">
                </form>
                </div>
            </div>
            <div class="col-lg-5"></div>
        </div>
    </body>
</html>
