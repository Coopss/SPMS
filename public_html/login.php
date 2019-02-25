<?php
    //Super basic login test code; modified from https://stackoverflow.com/questions/4115719/easy-way-to-password-protect-php-page
    $user = $_POST['user'];
    $pass = $_POST['password'];
    
    if($user == "spms" && $pass == "pass123") { //Successful login
            //$access = 10; validate referral
            include("./loggedin.php");
    }
    else {
        if(isset($_POST)) { //failed login
            /*include("./login.php?bad_login=1");*/
            echo "Bad credentials, please go back and try again";
            exit();
        }
        
        echo "This string should never appear";
    }

 ?>
 
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="style.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

	<title>SPMS: Login</title>
	<link rel="icon" href="./global_assets/favicon.png" type="image/png">


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
    
    <form class="login_form" action="login.php" method="post">
        <label for="user">Username</label>
        <input type="text" name="user"></input><br/><br>
        <label for="password">Password</label>
        <input type="password" name="password"></input><br><br>
        
        <button type="submit" name="submit">Login</button>
    </form>
    


<br><br>



</div>
<div class="footer_div">
    <?php include "footer_portfolio.html"; ?>
        <!-- Use jquery to insert header content here if PHP fails    -->
</div>

<script src='script.js'></script>

</body>
</html>
