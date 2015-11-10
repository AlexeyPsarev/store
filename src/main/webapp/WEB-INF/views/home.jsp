<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="js/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
        <link href="js/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
        <title></title>
        <style>
            .row {
                margin-left: 0px;
                margin-right: 0px;
            }
        </style>
    </head>
    <body>
        <!-- common -->
        <div>
            <p>Most popular</p>
            <img src="http://tomcat.apache.org/tomcat-8.0-doc/images/tomcat.png" height="128" width="128" style="margin-left: 10px">
            <img src="http://tomcat.apache.org/tomcat-8.0-doc/images/tomcat.png" height="128" width="128" style="margin-left: 10px">
            <img src="http://tomcat.apache.org/tomcat-8.0-doc/images/tomcat.png" height="128" width="128" style="margin-left: 10px">
            <img src="http://tomcat.apache.org/tomcat-8.0-doc/images/tomcat.png" height="128" width="128" style="margin-left: 10px">
            <img src="http://tomcat.apache.org/tomcat-8.0-doc/images/tomcat.png" height="128" width="128" style="margin-left: 10px">
        </div>
        <div class="row" style="margin-top: 20px;">
            <div class="col-md-3 text-center">
                <ul class="nav nav-pills nav-stacked">
                    <li role="presentation" class="active"><a href="#">Games</a></li>
                    <li role="presentation"><a href="#">Multimedia</a></li>
                </ul>
                <hr>
                <form action="upload.htm" method="GET">
                    <button class="btn btn-primary">Upload</button>
                    <input type="hidden" name="userId" value="${userId}">
                </form>
            </div>
            <div class="col-md-9" style="border-left: 2px solid; min-height: 650px">
                <div style="min-height: 650px">
                    <img src="http://tomcat.apache.org/tomcat-8.0-doc/images/tomcat.png" height="128" width="128">
                    <a href="" style="line-height: 128px; margin-left: 50px">Title1</a>
                    <hr>
                </div>
                <nav>
                    <ul class="pagination">
                        <li class="active"><a href="#">1</a></li>
                        <li><a href="#">2</a></li>
                        <li><a href="#">3</a></li>
                        <li><a href="#">4</a></li>
                        <li><a href="#">5</a></li>
                    </ul>
                </nav>
            </div>
        </div>
    </body>
</html>
