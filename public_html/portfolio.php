<!DOCTYPE html>
<html lang="en">
<head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="global_assets/portfolio.css">
        <link rel="stylesheet" type="text/css" href="global_assets/header.css">

        <title>Stock Overflow</title>
        <link rel="icon" href="./global_assets/img/favicon.png" type="image/png">

        <!-- CDN stuff for Bootstrap -->
        <script src="http://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

</head>
<body>

        <div class="header_div navbar-spacers">
            <?php include "header.php"; ?>
            <!-- Use jquery to insert header content here if PHP fails    -->
        </div>

<!-- Body of portfolio -->
<div class="">
        <div class="row">
                <div id="portfolio_table_div" class="offset-lg-1 col-lg-10 col-md-10 col-sm-10 col-xs-10">
                        <h3>Portfolio Transactions</h3>
                        <br>
                        
                        <!-- Table goes here -->
                        <table id="portfolio_table" class = "table stock_tables">
                           <tr>
                               <td>Loading data...</td>
                           </tr>
                           
                           
                           
                           <!--
                           <thead>
                                  <th> Sym </th>
                                  <th> Shares</th>
                                  <th> Last Price</th>
                                  <th> Change</th>
                                  <th> Change $ </th>
                                  <th> Pct. </th>
                                  <th> Total Cost</th>
                                  <th> Total Return</th>
                                  <th> Total Change</th>
                                  <th> Pct. </th>
                                  <th> CAGR </th>
                           </thead>

                           <tbody>
                              <tr>
                                      <td class="" id="Sym"> <a href="#">AMZN</a> </td>
                                      <td id="shares"> 300</td>
                                      <td id="price"> 1861.55</td>
                                      <td class="negative" id=change> -3.7 </td>
                                      <td class="negative" id="chgdol"> -$1,110.00</td>
                                      <td class="negative" id="pct1"> -0.17 </td>
                                      <td id="cost"> $260,942.00 </td>
                                      <td id="return"> $851,010.00</td>
                                      <td class="positive" id="chgtot"> +$590,068</td>
                                      <td class="positive"id="pct2"> +226.13%</td>
                                      <td class="positive" id="cagr"> +43.00%</td>
                              </tr>
                              <tr>
                                      <td id="Sym"> Total </td>
                                      <td id="shares"> 300</td>
                                      <td id="price"> 1861.55</td>
                                      <td class="negative" id=change> -3.7 </td>
                                      <td class="negative" id="chgdol"> -$1,110.00</td>
                                      <td class="negative" id="pct1"> -0.17 </td>
                                      <td id="cost"> $260,942.00 </td>
                                      <td id="return"> $851,010.00</td>
                                      <td class="positive" id="chgtot"> +$590,068</td>
                                      <td class="positive"id="pct2"> +226.13%</td>
                                      <td class="positive" id="cagr"> +43.00%</td>

                              </tr>
                           </tbody>
                            -->

                        </table>
                </div>
        </div>


</div>
<div class="footer_div">
    <?php include "footer.php"; ?>
        <!-- Use jquery to insert header content here if PHP fails    -->
</div>

<script src='script.js'></script>
<script type="text/javascript">
    portfolio();
</script>

</body>
</html>
