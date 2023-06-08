package pro17.se00;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVO {
	private int level;
	
	private int articleNO;
	
	private int parentNO;
	
	private String title;
	
	private String content;
	
	private String imageFileName;
	
	private String id;
	
	private Date writeDate;



	public void setImageFileName(String imageFileName) {
		try {
			if(imageFileName!=null && imageFileName.length()!=0) {
				this.imageFileName = URLEncoder.encode(imageFileName, "UTF-8");  //�����̸��� Ư�����ڰ� ���� ��� ���ڵ��մϴ�.
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	
	

}
