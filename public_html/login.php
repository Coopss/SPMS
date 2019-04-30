<!DOCTYPE html>
<html lang="en-US">
<head>
    <meta charset="utf-8" />
    <link rel="stylesheet" type="text/css" href="style.css">
    <link rel="stylesheet" type="text/css" href="global_assets/header.css">
    <link rel="stylesheet" type="text/css" href="global_assets/login.css">
    <title>SPMS | Login</title>
    <link rel="icon" href="./global_assets/img/favicon.png" type="image/png">

    <!-- CDN stuff for Bootstrap -->
    <script src="http://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>

<body class="theme-open-up">
<div id="react_root">
    <div>
        <div class="base">
            <div class="theme-open-up outer">
                <div class="texture"></div>
                <div class="login-outer">
                    <div class="login-box">
                        <div class="login-area">
                            <form class="form-lg"action="javascript:authenticate_user()" method="post">
                                <header>
                                    <a class="logo-margins" href="index.php">
                                        <img src="global_assets/img/logo.png" alt="Logo" height=40px>
                                    </a>
                                    <img class="logo-margins" src="global_assets/img/footer_logo.png" alt="Logo" height=40px>
                                </header>
                                <div class="form-vertical">
                                    <div>
                                        <div>
                                            <label for="user">
                                                    <div>Username</div>
                                                    <div>
                                                            <input autocomplete="off" autocorrect="off" autocapitalize="off" spellcheck="false" required="" name="user" type="text" value="" id='username'>
                                                    </div>
                                            </label>
                                        </div>
                                        <div>
                                            <label for="password">
                                                    <div>Password</div>
                                                    <div>
                                                            <input required="" name="password" type="password" value="" id='password'>
                                                    </div>
                                            </label>
                                        </div>
                                    </div>
                                    <p>
                                        <a class="create_link" href="signup.php">Create Account</a>
                                    </p>
                                </div>
                                <footer class="login-btn-area">
                                    <div>
                                        <button type="submit" name="submit" class="btn btn-primary login-btn">
                                            <span class="login-btn-text">Login</span>
                                        </button>
                                    </div>
                                </footer>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src='script.js'></script>

</body>
</html>
