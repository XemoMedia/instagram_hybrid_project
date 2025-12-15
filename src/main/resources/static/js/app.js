function save(){
 const fd=new FormData();
 fd.append('caption',caption.value);
 fd.append('scheduledTime',time.value);
 document.querySelectorAll('input[type=checkbox]:checked')
  .forEach(c=>fd.append('platforms',c.value));
 if(image.files[0]) fd.append('image',image.files[0]);
 if(video.files[0]) fd.append('video',video.files[0]);

 fetch('/api/posts',{method:'POST',body:fd}).then(load);
}

function load(){
 fetch('/api/posts').then(r=>r.json()).then(d=>{
  list.innerHTML='';
  d.forEach(p=>{
   let li=document.createElement('li');
   li.innerHTML=p.caption+' ['+p.status+']<br>Platforms: '+p.platforms.join(', ');
   list.appendChild(li);
  });
 });
}
load();
