<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

	<h1>OCR - 이미지에서 텍스트 추출하는 서비스</h1>
	<br>
	<br>
	<form id="frm">
		<label>이미지 파일</label>
		<input type="file" id="imgFile" name="imgFile">
		<button class="btn btn-info">파일 업로드</button>
	</form>
	<br><br>
	<hr>
	<h2>OCR - 이미지에서 텍스트 추출 결과</h2>
	<br>
	<div id="result">
	
	</div>
	
	<div id="imgDiv">
	
	</div>
	
<script>
	$(()=>{
		$('#frm').submit((e)=>{
			e.preventDefault();
			let form=$('#frm')[0];
			let formData = new FormData(form);
			let fileName=formData.get("imgFile").name;
			//alert(fileName);//첨부파일명
			if(!fileName){
				alert('이미지 파일을 첨부하세요');
				return;
			}
			$.ajax({
				type:'post',
				url:'ocrEnd',
				data:formData,
				dataType:'json',
				processData:false,
				contentType:false,
				cache:false
			})
			.done((res)=>{
				//alert(JSON.stringify(res));
				//$('#result').html(JSON.stringify(res));
				$('#result').html(`<h3>\${res.result}</h3>`).addClass('alert alert-danger');
				$('#imgDiv').empty().html("<img src ='upload/"+fileName+"' width='30%' >")
			})
			.fail((err)=>{
				alert('err')
			})
		})
	})

</script>