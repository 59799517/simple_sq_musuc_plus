import{I as E,X as y,J as c,W as l,n as _,L as z,M as b,x as S,P as G,A as p,F as I,bJ as L,bK as M,ai as k,aa as O,aI as T,S as D,bL as V,z as C}from"./index-8375bf6e.js";function W(e,n="default",a=[]){const o=e.$slots[n];return o===void 0?a:o()}const A=E("divider",`
 position: relative;
 display: flex;
 width: 100%;
 box-sizing: border-box;
 font-size: 16px;
 color: var(--n-text-color);
 transition:
 color .3s var(--n-bezier),
 background-color .3s var(--n-bezier);
`,[y("vertical",`
 margin-top: 24px;
 margin-bottom: 24px;
 `,[y("no-title",`
 display: flex;
 align-items: center;
 `)]),c("title",`
 display: flex;
 align-items: center;
 margin-left: 12px;
 margin-right: 12px;
 white-space: nowrap;
 font-weight: var(--n-font-weight);
 `),l("title-position-left",[c("line",[l("left",{width:"28px"})])]),l("title-position-right",[c("line",[l("right",{width:"28px"})])]),l("dashed",[c("line",`
 background-color: #0000;
 height: 0px;
 width: 100%;
 border-style: dashed;
 border-width: 1px 0 0;
 `)]),l("vertical",`
 display: inline-block;
 height: 1em;
 margin: 0 8px;
 vertical-align: middle;
 width: 1px;
 `),c("line",`
 border: none;
 transition: background-color .3s var(--n-bezier), border-color .3s var(--n-bezier);
 height: 1px;
 width: 100%;
 margin: 0;
 `),y("dashed",[c("line",{backgroundColor:"var(--n-color)"})]),l("dashed",[c("line",{borderColor:"var(--n-color)"})]),l("vertical",{backgroundColor:"var(--n-color)"})]),F=Object.assign(Object.assign({},b.props),{titlePlacement:{type:String,default:"center"},dashed:Boolean,vertical:Boolean}),q=_({name:"Divider",props:F,setup(e){const{mergedClsPrefixRef:n,inlineThemeDisabled:a}=z(e),i=b("Divider","-divider",A,L,e,n),o=S(()=>{const{common:{cubicBezierEaseInOut:r},self:{color:u,textColor:m,fontWeight:s}}=i.value;return{"--n-bezier":r,"--n-color":u,"--n-text-color":m,"--n-font-weight":s}}),t=a?G("divider",void 0,o,e):void 0;return{mergedClsPrefix:n,cssVars:a?void 0:o,themeClass:t==null?void 0:t.themeClass,onRender:t==null?void 0:t.onRender}},render(){var e;const{$slots:n,titlePlacement:a,vertical:i,dashed:o,cssVars:t,mergedClsPrefix:r}=this;return(e=this.onRender)===null||e===void 0||e.call(this),p("div",{role:"separator",class:[`${r}-divider`,this.themeClass,{[`${r}-divider--vertical`]:i,[`${r}-divider--no-title`]:!n.default,[`${r}-divider--dashed`]:o,[`${r}-divider--title-position-${a}`]:n.default&&a}],style:t},i?null:p("div",{class:`${r}-divider__line ${r}-divider__line--left`}),!i&&n.default?p(I,null,p("div",{class:`${r}-divider__title`},this.$slots),p("div",{class:`${r}-divider__line ${r}-divider__line--right`})):null)}}),H=()=>M,J={name:"Space",self:H},K=J;let w;const N=()=>{if(!k)return!0;if(w===void 0){const e=document.createElement("div");e.style.display="flex",e.style.flexDirection="column",e.style.rowGap="1px",e.appendChild(document.createElement("div")),e.appendChild(document.createElement("div")),document.body.appendChild(e);const n=e.scrollHeight===1;return document.body.removeChild(e),w=n}return w},U=Object.assign(Object.assign({},b.props),{align:String,justify:{type:String,default:"start"},inline:Boolean,vertical:Boolean,size:{type:[String,Number,Array],default:"medium"},wrapItem:{type:Boolean,default:!0},itemStyle:[String,Object],wrap:{type:Boolean,default:!0},internalUseGap:{type:Boolean,default:void 0}}),Q=_({name:"Space",props:U,setup(e){const{mergedClsPrefixRef:n,mergedRtlRef:a}=z(e),i=b("Space","-space",void 0,K,e,n),o=O("Space",a,n);return{useGap:N(),rtlEnabled:o,mergedClsPrefix:n,margin:S(()=>{const{size:t}=e;if(Array.isArray(t))return{horizontal:t[0],vertical:t[1]};if(typeof t=="number")return{horizontal:t,vertical:t};const{self:{[D("gap",t)]:r}}=i.value,{row:u,col:m}=V(r);return{horizontal:C(m),vertical:C(u)}})}},render(){const{vertical:e,align:n,inline:a,justify:i,itemStyle:o,margin:t,wrap:r,mergedClsPrefix:u,rtlEnabled:m,useGap:s,wrapItem:B,internalUseGap:R}=this,h=T(W(this));if(!h.length)return null;const $=`${t.horizontal}px`,v=`${t.horizontal/2}px`,P=`${t.vertical}px`,g=`${t.vertical/2}px`,f=h.length-1,x=i.startsWith("space-");return p("div",{role:"none",class:[`${u}-space`,m&&`${u}-space--rtl`],style:{display:a?"inline-flex":"flex",flexDirection:e?"column":"row",justifyContent:["start","end"].includes(i)?"flex-"+i:i,flexWrap:!r||e?"nowrap":"wrap",marginTop:s||e?"":`-${g}`,marginBottom:s||e?"":`-${g}`,alignItems:n,gap:s?`${t.vertical}px ${t.horizontal}px`:""}},!B&&(s||R)?h:h.map((j,d)=>p("div",{role:"none",style:[o,{maxWidth:"100%"},s?"":e?{marginBottom:d!==f?P:""}:m?{marginLeft:x?i==="space-between"&&d===f?"":v:d!==f?$:"",marginRight:x?i==="space-between"&&d===0?"":v:"",paddingTop:g,paddingBottom:g}:{marginRight:x?i==="space-between"&&d===f?"":v:d!==f?$:"",marginLeft:x?i==="space-between"&&d===0?"":v:"",paddingTop:g,paddingBottom:g}]},j)))}});export{Q as _,q as a};
