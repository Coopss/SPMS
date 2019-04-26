<nav class="navbar navbar-expand-md navbar-dark nav-background bg-mute">
        <button class="navbar-toggler collapsed" type="button" data-toggle="collapse" data-target="#navbarsExample04" aria-controls="navbarsExample04" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse row" id="navbarsExample04">
                <!-- Logo -->
                <div class="header_logo col-lg-2 col-md-2 col-sm-2 col-xs-2">
                        <a class="navbar-brand header-logo" href="index.php"><img src="global_assets/img/logo.png" alt="Logo" height="40px"></img></a>
                </div>
                <!-- Text box for search -->
                <div class="search-mds  col-lg-5 col-md-5 col-sm-5 col-xs-5  align_left">
                        <div class="float-left search_term" width="100%">
                                <form class="form-inline">
                                        <input class="form-control" type="text" placeholder="Search" list="search_list" id="search_term" onInput="JavaScript:search()" style="width:100%;" autoComplete="off"></input>
                                        <datalist id="search_list">
                                                <!-- option value="HTML">
                                                <option value="CSS"  -->
                                        </datalist>
                                </form>
                        </div>
                </div>

                <!-- Page links -->
                <div class="col-lg-5 col-md-5 col-sm-5 col-xs-5">
                        <ul class="z-back navbar-nav float-right">
                                <li class="nav-item">
                                        <a class="nav-link" href="index.php">Dashboard <span class="sr-only"></span></a>
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
        </div>
</nav>
