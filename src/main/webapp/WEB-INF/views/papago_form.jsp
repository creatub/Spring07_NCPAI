<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3>번역할 데이터 입력</h3>
<script>
$(()=>{
   $('#frm').on('submit',(e)=>{
      e.preventDefault();
      let str=$('input[name=ko]').val();
      if(!str){
         alert('내용 입력');
         return;
      }
      $.ajax({
         type:'post',
         url:'papago_trans',
         dataType:'json',
         data:{
            str:str
         },
         cache:false,
         success:function(res){
            alert(JSON.stringify(res));
            $('#result').html("<h3>"+res.message.result.translatedText+"</h3>")
            .addClass("alert alert-success");
         },
         error:function(err){
            alert("err: "+err);
         }
      })
   })
})//$() end
</script>
<form id="frm">
   <div class="my-3 py-3">
      <label>번역할 한글 문장 입력</label> <input type="text" name="ko"
         class="form-control">
   </div>
   <button class="btn btn-danger">한글 >> 영어</button>
</form>
<hr>
<div id="result">
<!-- 여기에 번역한 문장 나올 예정 -->
</div>
</body>
</html>