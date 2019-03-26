<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="style.css">
    <link rel="stylesheet" type="text/css" href="global_assets/header.css">
    <link rel="stylesheet" type="text/css" href="global_assets/login.css">

	<title>SPMS: Login</title>
	<link rel="icon" href="./global_assets/img/favicon.png" type="image/png">

    <!-- CDN stuff for Bootstrap -->
    <script src="http://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>

<div class="body_content">

        <div class="row">
                <div class="login-box offset-xl-10 col-lg-8">
                        <div class="container align-middle">
                                <div class="row justify-content-xl-center">
                                        <a class="logo-margins" href="index.php"><img src="global_assets/img/logo.png" alt="Logo" height=40px></a>
                                        <img class="logo-margins" src="global_assets/img/footer_logo.png" alt="Logo" height=40px>
                                </div>
                                <div class="row justify-content-xl-center">
                                        <form class="login_form login_background" action="javascript:authenticate_user()" method="post">
                                            <label for="user">Username</label>
                                            <input type="text" name="user" id='username'></input><br/><br>
                                            <label for="password">Password</label>
                                            <input type="password" name="password" id='password'></input><br><br>

                                            <button type="submit" name="submit">Login</button>
                                            <br>
                                            <a class="create_link" href="signup.php">Create Account</a>
                                        </form>
                                </div>
                        </div>




                        <div id="feedback"></div>
                </div>
        </div>

<br><br>



</div>

<script src='script.js'></script>

</body>
</html>
