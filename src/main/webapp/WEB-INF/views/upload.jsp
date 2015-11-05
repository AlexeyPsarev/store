<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="js/bootstrap-3.3.5-dist/js/bootstrap.min.js"></script>
        <link href="js/bootstrap-3.3.5-dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            .row {
                margin-left: 0px;
                margin-right: 0px;
                margin-top: 5px;
            }
        </style>
        <title>Upload</title>
    </head>
    <body>
        <button class="btn btn-link">Home</button><br>
        <div class="alert alert-danger">Alert</div>
        <form class="form-inline">
            <div class="row">
                <label class="col-md-2 text-right" style="line-height: 50px">Application name</label>
                <div class="col-md-4 text-left" style="line-height: 50px">
                    <input class="form-control" type="text" style="width: 100%">
                </div>
            </div>
            <div class="row">
                <label class="col-md-2 text-right">Application file</label>
                <div class="col-md-4 text-left">
                    <input type="file">
                    <p class="help-block">Choose the file to upload</p>
                </div>
            </div>
            <div class="row">
                <label class="col-md-2 text-right" style="line-height: 50px">Category</label>
                <div class="col-md-4 text-left" style="line-height: 50px">
                    <select class="form-control" style="width: 100%">
                        <option>Games</option>
                        <option>Multimedia</option>
                    </select>
                </div>
            </div>
            <div class="row">
                <label class="col-md-2 text-right">Description</label>
                <div class="col-md-4 text-left">
                    <textarea class="form-control" rows="10" style="width: 100%"></textarea>
                </div>
            </div>
            <div class="row">
                <div class="col-md-2"></div>
                <div class="col-md-4 text-center">
                    <button class="btn btn-success" style="width: 30%">Upload</button>
                </div>
            </div>
        </form>
    </body>
</html>
