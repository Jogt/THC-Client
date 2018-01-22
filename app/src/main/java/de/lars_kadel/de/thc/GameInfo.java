package de.lars_kadel.de.thc;


import android.os.Parcel;
import android.os.Parcelable;

public class GameInfo implements Parcelable{

    public final int prepTime;
    public final int gameTime;
    public final String[] detectives;
    public final String[] traitors;
    public final String name;
    public final Player.Role role;

    public GameInfo(int pt, int gt, String[] ds, String[] ts, String n, Player.Role r){
        this.prepTime = pt;
        this.gameTime = gt;
        this.detectives = ds;
        this.traitors = ts;
        this.name = n;
        this.role = r;
    }

    protected GameInfo(Parcel in) {
        prepTime = in.readInt();
        gameTime = in.readInt();
        detectives = in.createStringArray();
        traitors = in.createStringArray();
        name = in.readString();
        role = Player.Role.valueOf(in.readString());
    }

    public static final Creator<GameInfo> CREATOR = new Creator<GameInfo>() {
        @Override
        public GameInfo createFromParcel(Parcel in) {
            return new GameInfo(in);
        }

        @Override
        public GameInfo[] newArray(int size) {
            return new GameInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(prepTime);
        parcel.writeInt(gameTime);
        parcel.writeStringArray(detectives);
        parcel.writeStringArray(traitors);
        parcel.writeString(name);
        parcel.writeString(role.toString());
    }
}
