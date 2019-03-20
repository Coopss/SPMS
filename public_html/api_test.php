<!DOCTYPE html>
<html>
<head>
        <link rel="stylesheet" type="text/css" href="global_assets/dashboard.css">
        <link rel="stylesheet" type="text/css" href="global_assets/header.css">

        <title>Stock Overflow</title>
        <link rel="icon" href="./global_assets/img/logo.png" type="image/png">

        <!-- CDN stuff for Bootstrap -->
        <script
			  src="http://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">


</head>
<body>

    <div class="header_div navbar-spacers">
        <?php include "header.php"; ?>
        <!-- Use jquery to insert header content here if PHP fails    -->
    </div>

<!-- Body of index -->

<div class="body_content">
    <button type="button" name="ping_button" onclick="ping()">Ping!</button>
    <div id="ping_result"></div>
    <br>
    
    <input type="text" name="username" id='username' value="test123">
    <input type="password" name="password" id='password' value="asdf">
    <button type="button" name="validate_button" onclick="authenticate_user()">Validate user</button>
    <div id="validate_result"></div>
    <br>
    
    <button type="button" name="ping_button" onclick="is_authenticated()">Check authentication</button>
    <div id="auth_result"></div>
</div>
<br>

<div class="footer_div">
    <?php include "footer.php"; ?>
        <!-- Use jquery to insert header content here if PHP fails    -->
</div>

<script src='script.js'></script>

</body>
</html>
