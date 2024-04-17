<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<h1>Clova Voice Service</h1>
<br>
<div>
	<textarea id="txt" name="txt" rows="7" cols="60"
	placeholder="내용을 입력하면 음성으로 서비스 됩니다." class="form-control"></textarea>
	<br>
	<button class="btn btn-success" id="btn">음성 확인</button>
	
</div>
<hr>
<div class="alert alert-primary">
	<audio id="ttsAudio" controls="controls" preload="auto"></audio>
</div>

<script>

	$(()=>{
		$('#btn').click(()=>{
			let str=$('#txt').val();
			if(!str){
				alert('내용을 입력하세요');
				$('#txt').focus();
				return;
			}
			$.ajax({
				type:'post',
				url:'voiceToText',
				data:{
					content:str
				},
				dataType:'json'//네이버 응답유형은 blob ==>자바 컨트롤러에서 blob유형을 파일로 저장한 뒤 해당 파일명을 전송할 예정
			})
			.done((res)=>{
				//alert(JSON.stringify(res));
				$('#ttsAudio').prop("src","/upload/"+res.fileName);//파일명 설정
				$('#ttsAudio').prop("autoplay","autoplay");//자동 재생
			})
			.fail((err)=>{
				alert('err')
			})
		})
	})//$() end---------------------
</script>


