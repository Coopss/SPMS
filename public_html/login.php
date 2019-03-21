<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="style.css">
    <link rel="stylesheet" type="text/css" href="global_assets/header.css">

	<title>SPMS: Login</title>
	<link rel="icon" href="./global_assets/img/favicon.png" type="image/png">

    <!-- CDN stuff for Bootstrap -->
    <script src="http://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>

    <div class="header_div">
        <?php include "header.php"; ?>
        <!-- Use jquery to insert header content here if PHP fails    -->
    </div>

<div class="body_content">
    <div class="page_title">
        <br>
        <h1>Login Page</h1>
        <hr><br>
    </div>
    
    <form class="login_form" action="javascript:authenticate_user()" method="post">
        <label for="user">Username</label>
        <input type="text" name="user" id='username'></input><br/><br>
        <label for="password">Password</label>
        <input type="password" name="password" id='password'></input><br><br>
        
        <button type="submit" name="submit">Login</button>
        <br>
        <a href="signup.php">Create Account</a>
    </form>
    
    <div id="feedback"></div>

<br><br>



</div>
<div class="footer_div">
    <?php include "footer.html"; ?>
        <!-- Use jquery to insert header content here if PHP fails    -->
</div>

<script src='script.js'></script>

</body>
</html>
