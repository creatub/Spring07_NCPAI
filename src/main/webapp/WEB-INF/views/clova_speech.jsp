<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script type="text/javascript">
	$(()=>{
		$('#frm').submit((e)=>{
			e.preventDefault(); //submit되지 못하게 함
			let file=$('#mp3file')[0];
			if(file.files.length==0){
				alert('mp3파일을 첨부하세요');
				return;
			}
			//파일을 업로드할 때 FormData 객체 사용
			let form=$('#frm')[0];
			let formData=new FormData(form);
			let fileName = formData.get("mp3file").name;
			//alert(fileName);//첨부파일명
			
			$.ajax({
				type:'post',
				data:formData,
				url:'csrSpeech',
				dataType:'json',
				cache:false,
				processData:false,//true=> get방식으로 데이터 전달, false=>post방식
				contentType:false//false로 지정해야 enctype="multipart/form-data"로 전송할 수 있다.
			})
			.done((res)=>{
				//alert(JSON.stringify(res));
				//alert(res.result);//==> string 타입으로 옴
				let jsonObj = JSON.parse(res.result);
				//alert(jsonObj.text);
				$('#result').html("<h3>"+jsonObj.text+"</h3>")
							.addClass("alert alert-danger")
				//업로드한 오디오 파일 플레이 시키기
				$('#sttAudio').prop("src","/upload/"+fileName);//파일명 설정
				$('#sttAudio').prop("autoplay","autoplay");//자동 재생
				
			})
			.fail((err)=>{
				alert('err');
			})
		})//#frm----------------
	})//$() end-------------
</script>

<h1>
	Clova Speech Recognition
</h1>
<br>
<form id="frm">
	<select name="language">
		<option value="Kor">한국어</option>
		<option value="Eng">영어</option>
		<option value="Jpn">일어</option>
		<option value="Chn">중국어</option>
	</select>
	<br><br>
	<label for="mp3file">MP3파일</label>
	<input type="file" name="mp3file" id="mp3file">
	<button class="btn btn-primary">업로드</button>
</form>
<hr>
<br>
<div>
	<h2>STT(Speech To Text): 음성을 텍스트로 변환한 결과</h2>
	<br>
	<audio id="sttAudio" controls preload="auto"></audio>
</div>
<br><br>
<div id="result">


</div>