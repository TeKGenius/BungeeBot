package Events;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class AdminEvent extends ListenerAdapter {

    public static String prefix = ";";
    private int counter = 0;
    HashMap<String, Integer> user = new HashMap<>();

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] messageSent = event.getMessage().getContentRaw().split(" ");
        Member mentionedMember = event.getMessage().getMentionedMembers().get(0);
        Role verified = event.getGuild().getRolesByName("verified", true).get(0);



        if(messageSent[0].equalsIgnoreCase(prefix + "verify")){
            setYear(event, messageSent, mentionedMember);
            setRole(event, messageSent, mentionedMember);

            if(messageSent[1].equals("york")){
                event.getGuild().addRoleToMember(mentionedMember, verified).complete();
                event.getChannel().sendMessage("User has been Verified!").queue();
            }
            else{
                event.getGuild().addRoleToMember(mentionedMember, (Role) event.getGuild().getRolesByName("non-york", true)).complete();
                event.getGuild().addRoleToMember(mentionedMember, verified).complete();
                event.getChannel().sendMessage("User has been Verified!").queue();
            }

        }

        if(messageSent[0].equalsIgnoreCase(prefix + "warn")){
            String userID = mentionedMember.getId();

            if(!user.isEmpty()){
                if(user.containsKey(userID)){
                    if(user.get(userID) >= 3){
                        user.put(userID, user.get(userID) + 1);
                        store(userID);
                        event.getChannel().sendMessage("User has been warned "+ user.get(userID) +" times. Recommend Ban at this point.").queue();
                    }
                    else{
                        user.put(userID, user.get(userID) + 1);
                        store(userID);
                        event.getChannel().sendMessage("User has been warned "+ user.get(userID) +" times!").queue();
                    }
                }
                else{
                    user.put(userID, 1);
                    store(userID);
                    event.getChannel().sendMessage("User has been warned "+ user.get(userID) +" times!").queue();
                }
            }
            else{
                user.put(userID, 1);
                store(userID);
                event.getChannel().sendMessage("User has been warned "+ user.get(userID) +" times!").queue();
            }


        }

        if(messageSent[0].equalsIgnoreCase(prefix + "initiate")){
                initiate(user);
        }


    }

    private void initiate(HashMap<String, Integer> user) {
        File warnings = new File("warning.txt");
        ArrayList<String> userIDs = new ArrayList<>();

        try{
            Scanner input = new Scanner(warnings);
            while(input.hasNextLine()){
                userIDs.add(input.nextLine());
            }
            input.close();
        }
        catch (FileNotFoundException ignored) {
        }

        int counter = 0;

        for(int i = 0; i < userIDs.size(); i++){
            for(int j = 0; j < userIDs.size(); i++){
                if(userIDs.get(i).equals(userIDs.get(j))){
                    counter++;
                }
            }
            user.put(userIDs.get(i), counter);
        }


    }

    private void store(String userID) {
        File warnings = new File("warning.txt");
        try{
            warnings.createNewFile();
            FileWriter writer = new FileWriter(warnings, true);
            BufferedWriter writing = new BufferedWriter(writer);
            writing.write(userID);
            writing.newLine();
            writing.flush();
            writing.close();
        }
        catch (IOException ignored){
        }
    }

    private void setRole(GuildMessageReceivedEvent event, String[] messageSent, Member mentionedMember) {
        List<Role> role = event.getGuild().getRolesByName(messageSent[3],true);

        if(!role.isEmpty()){
            event.getGuild().addRoleToMember(mentionedMember, event.getGuild().getRolesByName(messageSent[3], true).get(0)).complete();
        }
        else{
            event.getChannel().sendMessage("Role does not exist, please try again").queue();
        }
    }

    private void setYear(GuildMessageReceivedEvent event, String[] messageSent, Member mentionedMember) {

        switch(messageSent[2]){
            case "1":
                event.getGuild().addRoleToMember(mentionedMember, event.getGuild().getRolesByName("1st-year",true).get(0)).complete();
                break;
            case "2":
                event.getGuild().addRoleToMember(mentionedMember, event.getGuild().getRolesByName("2nd-year",true).get(0)).complete();
                break;
            case "3":
                event.getGuild().addRoleToMember(mentionedMember, event.getGuild().getRolesByName("3rd-year",true).get(0)).complete();
                break;
            case "4":
                event.getGuild().addRoleToMember(mentionedMember, event.getGuild().getRolesByName("4th-year",true).get(0)).complete();
                break;
            case "alumnus":
                event.getGuild().addRoleToMember(mentionedMember, event.getGuild().getRolesByName("alumnus",true).get(0)).complete();
                break;
            default:
                event.getGuild().addRoleToMember(mentionedMember, event.getGuild().getRolesByName("5th-year-plus",true).get(0)).complete();
                break;
        }
    }

    private void banMember(Member member, GuildMessageReceivedEvent event){
        event.getGuild().ban(member,0).queue();
    }


}
