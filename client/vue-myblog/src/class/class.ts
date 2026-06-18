
export class User{
    userName:string;
    phone:string;
    email:string;
    password:string;

    
    constructor(userName:string,phone:string,email:string,password:string) {
        this.userName=userName
        this.phone=phone
        this.email=email
        this.password=password
      }


    getUserName(){
        return this.userName
    }
    setUserName(name:string){
        this.userName=name  
    }


    getPhone(){
        return this.phone
    }
    setPhone(phone:string){
        this.phone=phone  
    }

    getEmail(){
        return this.email
    }
    setEmail(email:string){
        this.email=email  
    }
    getPassword(){
        return this.password
    }
    setPassword(password:string){
        this.password=password  
    }

    getAll(){

        return this.userName,this.phone,this.email
    }
    
}

export class Article{
    articleTitle:string
    aricleSign:string
    articleContent:string

    authorId:number
    authorName:string

    constructor(articleTitle:string, aricleSign:string,articleContent:string,authorId:number,authorName:string){

        this.articleTitle=articleTitle
        this.aricleSign=aricleSign
        this.articleContent=articleContent
        this.authorId=authorId
        this.authorName=authorName

    }

    getArticleTitle(){
        return this.articleTitle
    }
    setArticleTitle(articleTitle:string){
        this.articleTitle=articleTitle
    }

    
    getAaricleSign(){
        return this.aricleSign
    }
    setaricleSign(aricleSign:string){
        this.aricleSign=aricleSign
    }

    getarticleContent(){
        return this.articleContent
    }
    setarticleContent(articleContent:string){
        this.articleContent=articleContent
    }


    
}

export class ArticleList{
    articleTitle:string
    aricleSign:string
    authorId:number
    authorName:string
    createTime:string

    constructor(articleTitle:string,aricleSign:string,authorId:number,authorName:string,createTime:string){

        this.articleTitle=articleTitle
        this.aricleSign=aricleSign
        this.authorId=authorId
        this.authorName=authorName
        this.createTime=createTime

    }



}






