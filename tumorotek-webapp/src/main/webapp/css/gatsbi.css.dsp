div.main-container {
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

div.block-container {
  background: none;
  display: flex;
  flex-flow: row wrap;
  align-content: center;
  justify-content: space-between;
}

div.item {
	padding: 5px 5px 5px 6px; 
	background: none;  
  	min-height: 20px;  
  	font-size: 18px;  
  	text-align: left;
  	display: flex;
  	flex-direction: row;
  	align-content: center;
  	justify-content: start;
}

div.block-title {
	width: 100%;
}

div.item-mid {
  width: 47%;   
}

div.item-wide {
	width: 95%;
}

.item:nth-child(2n){
  break-after: always;
}

div.item > .z-datebox {
	display: inline-flex;
	align-items: center;
}

div.item > .z-select {
	height: 25px;
}

div.row-container {
  display: flex;
  flex-direction: row;
  align-content: center;
  justify-content: start;
}

div.column-container {
  display: flex;
  flex-direction: column;
  align-content: center;
  justify-content: start;
}

div.column-container > * {
	margin-bottom: 5px;
}

div.row-container > span {
	padding-right: 2px;
}

div.block-separator {
	height: 15px;
}

div.item .chpLabel, div.item span.formValue, div.item span.formLink {
	width: 150px;
	display: inline-block;
  	align-items: start;
  	line-height: 20px;
}

div.gridForm1 div.item {
	color : #636363; 
	font-weight : bold; 
	font-family: Verdana, Arial, Helvetica, sans-serif;
}

div.gridForm1 > div.item-disabled {
	color : #7F7F7F; 
	font-weight : normal; 
	font-style: italics;
	font-family: Verdana, Arial, Helvetica, sans-serif;
	border-bottom-style : solid; 
	border-bottom-width : 1px;
	border-bottom-color : #7F7F7F;
}

div.gridForm1 .z-group-box{	
	border-bottom-width : 2px; /*PRO*/
	border-bottom-color : #E03F06; /*PRO*/
	border-top-style : none; /*PRO*/
	border-right-style : none; /*PRO*/
	border-left-style : none; /*PRO*/
	border-top-width : 1px; /*PRO*/
	border-top-color : #E03F06; /*PRO*/
}

/* required field */
div.item span.requiredMark {
	display: none;
}

div.item-required span.requiredMark {
	display: inline;
	width: 5px;
}

div.item .changedValue {
	flex-grow: 1; 
	align-content: right;
}

div.item .modifmultiValue {
	text-align: right;
	font-style: italic; 
}

div.main-container .z-groupbox-header {
	color: #00227c;
	font-weight: bold;
	font-size: 12px;
}