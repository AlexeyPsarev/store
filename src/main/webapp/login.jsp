<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="js/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
        <link href="js/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
        <title>Login page</title>
    </head>
    <body>
        <button class="btn btn-link">Back</button><br>
        <div class="alert alert-danger">Alert</div>
        <form class="form-inline">
            <div class="row">
                <label class="col-md-2 text-right" style="line-height: 50px">Username</label>
                <div class="col-md-2 text-left" style="line-height: 50px">
                    <input class="form-control" type="text" style="width: 100%" autocomplete="off">
                </div>
            </div>
            <div class="row">
                <label class="col-md-2 text-right" style="line-height: 50px">Password</label>
                <div class="col-md-2 text-left" style="line-height: 50px">
                    <input class="form-control" type="password" style="width: 100%">
                </div>
            </div>
            <div class="row">
                <div class="col-md-2"></div>
                <div class="col-md-2 text-center">
                    <button class="btn btn-primary" style="width: 30%">Submit</button>
                </div>
            </div>
        </form>
    </body>
</html>
