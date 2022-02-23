package com.example.projectj.graphQL.Room;

import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Log
@Component
@RequiredArgsConstructor
@Transactional
public class RoomMutation implements GraphQLMutationResolver {
}
