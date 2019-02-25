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
        <h1>Signup Page</h1>
        <hr><br>
    </div>
    
    <form class="login_form" action="signup.php" method="post">
        <label for="email">Email</label>
        <input type="email" name="email"></input><br/><br>
        
        <label for="user">Username</label>
        <input type="text" name="user"></input><br/><br>
        
        <label for="password">Password</label>
        <input type="password" name="password"></input>
        <label for="password_confirm">Confirm Password</label>
        <input type="password" name="password_confirm"></input><br><br>
        
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
