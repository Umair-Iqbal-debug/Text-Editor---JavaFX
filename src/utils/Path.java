package utils;

public class Path 
{
	
	private static String convertPathToLinuxFormat(String path)
    {
        StringBuilder sb = new StringBuilder();
        String[] temp = path.split("\\\\");

        for(String word: temp)
        {
            sb.append(word +"/");
        }

        return sb.toString();
    }
	
	public static String getProjectPath()
	{
		return convertPathToLinuxFormat(System.getProperty("user.dir"));
	}

}
