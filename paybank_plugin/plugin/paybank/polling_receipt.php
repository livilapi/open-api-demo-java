<?php
include_once('./_common.php');

if (1 == is_numeric($_REQUEST['ORN'])) {
  $sql = " SELECT po_rel_action AS RECEIPT FROM g5_point WHERE po_rel_action IN ('PayBank-ORN-{$_REQUEST['ORN']}') ";
  $result = sql_query($sql);
  $row = sql_fetch_array($result);
  
  echo $row['RECEIPT'];
}
else {
  exit;
}
?>
