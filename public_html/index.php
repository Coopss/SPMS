<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="global_assets/dashboard.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

	<title>Stock Overflow</title>
	<link rel="icon" href="./global_assets/img/logo.png" type="image/png">

        <!-- CDN stuff for Bootstrap -->
        <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">


</head>
<body>

    <div class="header_div">
        <?php include "header.php"; ?>
        <!-- Use jquery to insert header content here if PHP fails    -->
    </div>

<!-- Body of index -->
<div class="container mt-1">
        <div class="col-lg-11">
                <div class="row">

                        <div class="col-lg-8 bar-right">
                                <h3>Summary</h3>
                                <img src="global_assets/img/graph.png" class="img-responsive" width=620px margin-left=10px>
                        </div>
                        <div class="col-lg-3">
                                <!-- List of Stocks -->
                                <table class = "table">
                                        <h4> Stocks </h4>
                                   <thead>
                                      <tr>
                                         <th>Stock</th>
                                         <th>Shares</th>
                                         <th>Graph</th>
                                         <th>Price</th>
                                      </tr>
                                   </thead>

                                   <tbody>
                                      <tr>
                                         <td>AMD</td>
                                         <td>2</td>
                                         <td>Graph</td>
                                         <td>Price</td>
                                      </tr>
                                   </tbody>
                                </table>

                                <!-- Watchlist -->
                                <table class = "table">
                                         <h4> Watch List </h4>
                                   <thead>
                                      <tr>
                                         <th>Stock</th>
                                         <th>Graph</th>
                                         <th>Price</th>
                                      </tr>
                                   </thead>

                                   <tbody>
                                      <tr>
                                         <td>SNE</td>
                                         <td>Graph</td>
                                         <td>Price</td>
                                      </tr>
                                   </tbody>

                                </table>
                        </div>
                </div>
                <div>
                        <h3>Top Movers</h3>
                        <p> Articles go here </p>
                </div>
                <div>
                        <h3>Relevant articles</h3>
                        <div class="row">
                                <div class="col-lg-2">
                                        <a href="#" class="previous round arrow">Previous &laquo</a>
                                </div>
                                <div class="col-lg-7">
                                        <h3> Articles </h3>
                                </div>
                                <div class="col-lg-2">
                                        <a href="#" class="next round mx-righ arrowt"> Next &raquo</a>
                                </div>
                        </div>

                </div>
        </div>

</div>
<div class="footer_div">
    <?php include "footer.php"; ?>
        <!-- Use jquery to insert header content here if PHP fails    -->
</div>

<script src='script.js'></script>

</body>
</html>

<!---
<div class="col-md-8">
          <div class="row">
            <div class="col-md-6">.col-md-6</div>
            <div class="col-md-6">.col-md-6</div>
          </div>
        </div>
-->
