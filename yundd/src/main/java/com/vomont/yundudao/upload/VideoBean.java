package com.vomont.yundudao.upload;

public class VideoBean
{
    private String name;
    
    private String subname;
    
    private int subfatoryid;
    
    private byte[] img;
    
    private long creattime;
    
    private String desp;
    
    private String looker;
    
    private String lookername;
    
    private int isPack;
    
    private int loadstate;
    
    private int pos;
    
    private int videoid;
    
    private int delete;
    
    private String videoPath;
   

    
    public String getVideoPath()
    {
        return videoPath;
    }

    public void setVideoPath(String videoPath)
    {
        this.videoPath = videoPath;
    }

    public int getDelete()
    {
        return delete;
    }

    public void setDelete(int delete)
    {
        this.delete = delete;
    }

    public int getVideoid()
    {
        return videoid;
    }

    public void setVideoid(int videoid)
    {
        this.videoid = videoid;
    }

    public int getPos()
    {
        return pos;
    }

    public void setPos(int pos)
    {
        this.pos = pos;
    }

    public int getLoadstate()
    {
        return loadstate;
    }

    public void setLoadstate(int loadstate)
    {
        this.loadstate = loadstate;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSubname()
    {
        return subname;
    }

    public void setSubname(String subname)
    {
        this.subname = subname;
    }

    public int getSubfatoryid()
    {
        return subfatoryid;
    }

    public void setSubfatoryid(int subfatoryid)
    {
        this.subfatoryid = subfatoryid;
    }

    public byte[] getImg()
    {
        return img;
    }

    public void setImg(byte[] img)
    {
        this.img = img;
    }

    public long getCreattime()
    {
        return creattime;
    }

    public void setCreattime(long creattime)
    {
        this.creattime = creattime;
    }

    public String getDesp()
    {
        return desp;
    }

    public void setDesp(String desp)
    {
        this.desp = desp;
    }

    public String getLooker()
    {
        return looker;
    }

    public void setLooker(String looker)
    {
        this.looker = looker;
    }

    public String getLookername()
    {
        return lookername;
    }

    public void setLookername(String lookername)
    {
        this.lookername = lookername;
    }

    public int getIsPack()
    {
        return isPack;
    }

    public void setIsPack(int isPack)
    {
        this.isPack = isPack;
    }
    
    
}
