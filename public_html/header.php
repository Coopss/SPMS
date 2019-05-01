<nav class="navbar navbar-expand-md navbar-dark nav-background bg-mute navbar-fixed-top">
        <!-- Logo -->
        <a class="navbar-brand header-logo ml-5 mr-5" href="index.php">
                <img src="global_assets/img/logo.png" alt="Logo" height="40px"></img>
        </a>
        <button class="navbar-toggler collapsed align-right" type="button" data-toggle="collapse" data-target="#navbarsExample04" aria-controls="navbarsExample04" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse row ml-5 pl-5" id="navbarsExample04">
                <!-- Text box for search -->
                <form class="form-inline mr-auto">
                        <input class="form-control" type="text" placeholder="Search" list="search_list" id="search_term" onInput="JavaScript:search()" style="width:33vw;" autoComplete="off"></input>
                        <datalist id="search_list">
                                <!-- option value="HTML">
                                <option value="CSS"  -->
                        </datalist>
                </form>
                <!-- Page links -->
                <ul class="z-back navbar-nav ml-auto">
                        <li class="nav-item">
                                <a class="nav-link" href="index.php">Dashboard
                                        <span class="sr-only"></span>
                                </a>
                        </li>
                        <li class="nav-item">
                                <a class="nav-link" href="portfolio.php">Porfolio</a>
                        </li>
                        <!-- Login link or username -->
                        <li class="nav-item">
                                <a href="#" class="dropdown-toggle nav-link" data-toggle="dropdown">
                                        <span class="" id='display_name'></span>
                                </a>
                                <ul id="login-dp" class="dropdown-menu dropdown-menu-right">
                                        <li class="bg-mute">
                                                <div class="row">
                                                        <div class="col-lg-12 mx-right">
                                                                <a class="nav-link" href='javascript:logout()'>Log Out</a>
                                                        </div>
                                                </div>
                                        </li>
                                </ul>
                        </li>
                </ul>
        </div>
</nav>
